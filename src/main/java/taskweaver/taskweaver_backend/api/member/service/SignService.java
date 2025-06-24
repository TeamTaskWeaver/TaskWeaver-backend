package taskweaver.taskweaver_backend.api.member.service;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import taskweaver.taskweaver_backend.api.member.controller.request.AccessTokenResponse;
import taskweaver.taskweaver_backend.api.member.controller.request.SignInRequest;
import taskweaver.taskweaver_backend.api.member.controller.request.SignUpRequest;
import taskweaver.taskweaver_backend.api.member.service.converter.MemberConverter;
import taskweaver.taskweaver_backend.api.member.service.response.OauthSignUpResponse;
import taskweaver.taskweaver_backend.api.member.service.response.SignInResponse;
import taskweaver.taskweaver_backend.api.member.service.response.SignUpResponse;
import taskweaver.taskweaver_backend.auth.CookieUtil;
import taskweaver.taskweaver_backend.auth.TokenProvider;
import taskweaver.taskweaver_backend.common.code.ErrorCode;
import taskweaver.taskweaver_backend.common.exception.handler.BusinessExceptionHandler;
import taskweaver.taskweaver_backend.domain.member.model.*;
import taskweaver.taskweaver_backend.domain.member.oauth.KakaoProfile;
import taskweaver.taskweaver_backend.domain.member.oauth.OauthToken;
import taskweaver.taskweaver_backend.domain.member.repository.MemberPlatformRepository;
import taskweaver.taskweaver_backend.domain.member.repository.MemberRefreshTokenRepository;
import taskweaver.taskweaver_backend.domain.member.repository.MemberRepository;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SignService {
    private final MemberRepository memberRepository;
    private final MemberRefreshTokenRepository memberRefreshTokenRepository;
    private final MemberPlatformRepository memberPlatformRepository; // MemberPlatformRepository 주입 추가
    private final PasswordEncoder encoder;
    private final TokenProvider tokenProvider;
    private final CookieUtil cookieUtil;

    @Value("${kakaoApiKey}")
    private String kakaoApiKey;

    @Value("${kakaoRedirectUrl}")
    private String redirectURI;


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

        memberRefreshTokenRepository.findByMemberId(member.getId())
                .ifPresentOrElse(
                        it -> it.updateRefreshToken(refreshToken),
                        () -> memberRefreshTokenRepository.save(new MemberRefreshToken(member, refreshToken))
                );

        return MemberConverter.toSignInResponse(member, accessToken, refreshToken);
    }

    @Transactional
    public SignInResponse getKakaoAccessToken(String code) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoApiKey);
        params.add("redirect_uri", "http://localhost:3000/login/callback");
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
            throw new RuntimeException(e);
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }

        System.out.println("카카오가 응답: " + accessTokenResponse.getBody());


        ObjectMapper objectMapper = new ObjectMapper();
        OauthToken oauthToken;
        try {
            oauthToken = objectMapper.readValue(accessTokenResponse.getBody(), OauthToken.class);
        } catch (JsonProcessingException e) {
            throw new BusinessExceptionHandler(ErrorCode.KAKAO_TOKEN_PARSE_FAILED);
        }

        KakaoProfile kakaoProfile = findKakaoProfile(oauthToken.getAccessToken());

        String providerId = String.valueOf(kakaoProfile.getId());

        Member member;
        MemberPlatform memberPlatform = memberPlatformRepository
                .findByProviderAndProviderId(ProviderType.KAKAO, providerId)
                .orElse(null);

        if (memberPlatform == null) {
            String temporaryEmail = "kakao_" + providerId + "@example.com"; // 임시 이메일
            String temporaryPassword = encoder.encode(UUID.randomUUID().toString());
            String temporaryNickname = "카카오사용자_" + providerId.substring(0, Math.min(6, providerId.length())); // 임시 닉네임

            member = Member.builder()
                    .email(temporaryEmail)
                    .password(temporaryPassword)
                    .nickname(temporaryNickname)
                    .loginType(LoginType.KAKAO)
                    .build();
            member = memberRepository.save(member);


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


        String accessToken = tokenProvider.createAccessToken(
                String.format("%s:%s", finalMember.getId(), finalMember.getLoginType())
        );
        String refreshToken = tokenProvider.createRefreshToken();


        memberRefreshTokenRepository.findByMemberId(finalMember.getId())
                .ifPresentOrElse(
                        it -> it.updateRefreshToken(refreshToken),
                        () -> memberRefreshTokenRepository.save(new MemberRefreshToken(finalMember, refreshToken))
                );



        return MemberConverter.toSignInResponse(finalMember, accessToken, refreshToken);
    }


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

    public AccessTokenResponse reissueAccessToken(String refreshToken, HttpServletResponse response) {
        // 1. RefreshToken 검증
        if (refreshToken == null || !tokenProvider.isTokenValid(refreshToken)) {
            throw new BusinessExceptionHandler(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 2. DB에 저장된 RefreshToken과 일치하는지 확인
        MemberRefreshToken memberRefreshToken = memberRefreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        // 3. 토큰에서 사용자 정보 추출
        Member member = memberRefreshToken.getMember();

        // 4. 새로운 AccessToken 생성
        String newAccessToken = tokenProvider.createAccessToken(
                String.format("%s:%s", member.getId(), member.getLoginType())
        );

        // 5. (보안 강화) Refresh Token Rotation (RTR): 기존 RefreshToken은 무효화하고 새로운 RefreshToken 발급
        String newRefreshToken = tokenProvider.createRefreshToken();
        memberRefreshToken.updateRefreshToken(newRefreshToken); // DB에 새 RefreshToken으로 업데이트
        memberRefreshTokenRepository.save(memberRefreshToken); // 변경된 내용 저장

        // 6. 새로 발급한 RefreshToken을 쿠키에 담아 응답
        long refreshTokenMaxAgeSeconds = 7 * 24 * 60 * 60; // 예: 7일
        cookieUtil.createRefreshTokenCookie(response, newRefreshToken, refreshTokenMaxAgeSeconds);

        // 7. 새로운 AccessToken을 DTO에 담아 반환
        return AccessTokenResponse.builder()
                .accessToken(newAccessToken)
                .build();
    }



}
