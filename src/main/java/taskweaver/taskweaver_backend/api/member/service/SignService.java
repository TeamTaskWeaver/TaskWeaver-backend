package taskweaver.taskweaver_backend.api.member.service;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import taskweaver.taskweaver_backend.api.member.controller.request.SignInRequest;
import taskweaver.taskweaver_backend.api.member.controller.request.SignUpRequest;
import taskweaver.taskweaver_backend.api.member.service.converter.MemberConverter;
import taskweaver.taskweaver_backend.api.member.service.response.OauthSignUpResponse;
import taskweaver.taskweaver_backend.api.member.service.response.SignInResponse;
import taskweaver.taskweaver_backend.api.member.service.response.SignUpResponse;
import taskweaver.taskweaver_backend.auth.TokenProvider;
import taskweaver.taskweaver_backend.common.code.ErrorCode;
import taskweaver.taskweaver_backend.common.exception.handler.BusinessExceptionHandler;
import taskweaver.taskweaver_backend.domain.member.model.*;
import taskweaver.taskweaver_backend.domain.member.oauth.KakaoProfile;
import taskweaver.taskweaver_backend.domain.member.oauth.OauthToken;
import taskweaver.taskweaver_backend.domain.member.repository.MemberPlatformRepository;
import taskweaver.taskweaver_backend.domain.member.repository.MemberRefreshTokenRepository;
import taskweaver.taskweaver_backend.domain.member.repository.MemberRepository;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SignService {
    private final MemberRepository memberRepository;
    private final MemberRefreshTokenRepository memberRefreshTokenRepository;
    private final MemberPlatformRepository memberPlatformRepository; // MemberPlatformRepository 주입 추가
    private final PasswordEncoder encoder;
    private final TokenProvider tokenProvider;

    @Value("${kakaoApiKey}")
    private String kakaoApiKey;

    @Value("${kakaoRedirectUrl}")
    private String redirectURI;

    // 회원가입
    @Transactional
    public SignUpResponse registerMember(SignUpRequest request) {
        // 임시 비밀번호 패턴 체크
        if (request.password().startsWith("TEMP-")) {
            throw new BusinessExceptionHandler(ErrorCode.NOT_ALLOWED_PASSWORD);
        }

        try {
            Member member = MemberConverter.toMember(request, encoder);
            member = memberRepository.saveAndFlush(member);
            return MemberConverter.toSignUpResponse(member);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessExceptionHandler(ErrorCode.DUPLICATED_EMAIL);
        }
    }

    // 로그인
    @Transactional
    public SignInResponse signIn(SignInRequest request) throws JsonProcessingException {
        Member member = memberRepository.findByEmail(request.email())
                .filter(it -> encoder.matches(request.password(), it.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

        String accessToken = tokenProvider.createAccessToken(
                String.format("%s:%s", member.getId(), member.getLoginType())
        );
        String refreshToken = tokenProvider.createRefreshToken();

        // memberRefreshTokenRepository.findById(member.getId()) // 변경 전: MemberRefreshToken의 PK로 조회
        memberRefreshTokenRepository.findByMemberId(member.getId()) // 변경 후: member_id (Foreign Key)로 조회
                .ifPresentOrElse(
                        it -> it.updateRefreshToken(refreshToken),
                        () -> memberRefreshTokenRepository.save(new MemberRefreshToken(member, refreshToken))
                );

        return MemberConverter.toSignInResponse(member, accessToken, refreshToken);
    }

    // 카카오 OAuth 토큰 발급 및 서비스 로그인/회원가입 처리
    @Transactional // DB 저장 로직이 포함되므로 @Transactional 추가
    public SignInResponse getKakaoAccessToken(String code) { // 반환 타입을 SignInResponse로 변경
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoApiKey);
        params.add("redirect_uri", redirectURI);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        ResponseEntity<String> accessTokenResponse;
        try {
            accessTokenResponse = rt.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );
        } catch (HttpClientErrorException e) {
            // 4xx 클라이언트 오류 (예: 400 Bad Request - KOE320, KOE303 등)
            throw new RuntimeException(e); // BusinessExceptionHandler 제거
        } catch (RestClientException e) {
            // 네트워크 연결 문제 등 RestTemplate 자체 오류
            throw new RuntimeException(e); // BusinessExceptionHandler 제거
        }


        ObjectMapper objectMapper = new ObjectMapper();
        OauthToken oauthToken;
        try {
            oauthToken = objectMapper.readValue(accessTokenResponse.getBody(), OauthToken.class);
        } catch (JsonProcessingException e) {
            throw new BusinessExceptionHandler(ErrorCode.KAKAO_TOKEN_PARSE_FAILED);
        }

        // 카카오 프로필 조회
        KakaoProfile kakaoProfile = findKakaoProfile(oauthToken.getAccess_token());

        // kakaoProfile.getId()는 카카오 사용자의 고유 ID입니다.
        String providerId = String.valueOf(kakaoProfile.getId()); // providerId는 String이므로 변환

        Member member;
        // 1. MemberPlatform으로 기존 회원 조회
        // ProviderType.KAKAO와 providerId를 사용하여 MemberPlatform을 찾습니다.
        MemberPlatform memberPlatform = memberPlatformRepository
                .findByProviderAndProviderId(ProviderType.KAKAO, providerId)
                .orElse(null);

        if (memberPlatform == null) {
            // 2. 신규 회원인 경우 (MemberPlatform이 없는 경우)
            // 새로운 Member 생성 후 MemberPlatform도 생성하여 연결합니다.

            // Member 엔티티의 email과 nickname이 nullable=false 이므로 임시 값 설정
            // 이메일은 카카오ID 기반으로 임시 생성하거나, 정책에 따라 비워둘 수 있습니다.
            // 닉네임은 추후 사용자에게 입력받을 예정이므로 임시 값을 설정합니다.
            String temporaryEmail = "kakao_" + providerId + "@example.com"; // 임시 이메일
            String temporaryPassword = encoder.encode(UUID.randomUUID().toString()); // 안전한 임시 패스워드 생성
            String temporaryNickname = "카카오사용자_" + providerId.substring(0, Math.min(6, providerId.length())); // 임시 닉네임

            // Member 엔티티 생성
            member = Member.builder()
                    .email(temporaryEmail)
                    .password(temporaryPassword)
                    .nickname(temporaryNickname) // 임시 닉네임 설정
                    .loginType(LoginType.KAKAO)
                    .build();
            member = memberRepository.save(member); // Member 저장

            // MemberPlatform 엔티티 생성 및 연결
            memberPlatform = MemberPlatform.builder()
                    .member(member)
                    .provider(ProviderType.KAKAO)
                    .providerId(providerId)
                    .build();
            memberPlatformRepository.save(memberPlatform); // MemberPlatform 저장

        } else {
            // 3. 기존 회원인 경우 (MemberPlatform이 존재하는 경우)
            // MemberPlatform에서 연결된 Member를 가져옵니다.
            member = memberPlatform.getMember();
        }

        final Member finalMember = member;

        // 4. 로그인 처리 및 JWT 토큰 발급
        String accessToken = tokenProvider.createAccessToken(
                String.format("%s:%s", finalMember.getId(), finalMember.getLoginType())
        );
        String refreshToken = tokenProvider.createRefreshToken();

        // memberRefreshTokenRepository.findById(finalMember.getId()) // 변경 전: MemberRefreshToken의 PK로 조회
        memberRefreshTokenRepository.findByMemberId(finalMember.getId()) // 변경 후: member_id (Foreign Key)로 조회
                .ifPresentOrElse(
                        it -> it.updateRefreshToken(refreshToken),
                        () -> memberRefreshTokenRepository.save(new MemberRefreshToken(finalMember, refreshToken))
                );

        // JWT 토큰이 포함된 SignInResponse 반환
        return MemberConverter.toSignInResponse(finalMember, accessToken, refreshToken);
    }

    // 카카오 프로필 조회
    public KakaoProfile findKakaoProfile(String token) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<Void> kakaoProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> kakaoProfileResponse;
        try {
            kakaoProfileResponse = rt.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    kakaoProfileRequest,
                    String.class
            );
        } catch (HttpClientErrorException e) {
            throw new RuntimeException(e);
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }


        String rawJsonResponse = kakaoProfileResponse.getBody();
        System.out.println("--- Raw Kakao Profile JSON Response ---");
        System.out.println(rawJsonResponse);
        System.out.println("-------------------------------------");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(rawJsonResponse, KakaoProfile.class);
        } catch (JsonProcessingException e) {
            throw new BusinessExceptionHandler(ErrorCode.KAKAO_PROFILE_PARSE_FAILED);
        }
    }
}
