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
import taskweaver.taskweaver_backend.domain.member.model.Member;
import taskweaver.taskweaver_backend.domain.member.model.MemberRefreshToken;
import taskweaver.taskweaver_backend.domain.member.oauth.KakaoProfile;
import taskweaver.taskweaver_backend.domain.member.oauth.OauthToken;
import taskweaver.taskweaver_backend.domain.member.repository.MemberRefreshTokenRepository;
import taskweaver.taskweaver_backend.domain.member.repository.MemberRepository;
@RequiredArgsConstructor
@Service
public class SignService {
    private final MemberRepository memberRepository;
    private final MemberRefreshTokenRepository memberRefreshTokenRepository;
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

        memberRefreshTokenRepository.findById(member.getId())
                .ifPresentOrElse(
                        it -> it.updateRefreshToken(refreshToken),
                        () -> memberRefreshTokenRepository.save(new MemberRefreshToken(member, refreshToken))
                );

        // Redis 관련 로직이 모두 제거되었습니다.

        return MemberConverter.toSignInResponse(member, accessToken, refreshToken);
    }

    // 카카오 OAuth 토큰 발급
    public Object getKakaoAccessToken(String code) {
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

        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        OauthToken oauthToken;
        try {
            oauthToken = objectMapper.readValue(accessTokenResponse.getBody(), OauthToken.class);
        } catch (JsonProcessingException e) {
            throw new BusinessExceptionHandler(ErrorCode.KAKAO_TOKEN_PARSE_FAILED);
        }

        String email = findKakaoProfile(oauthToken.getAccess_token())
                .getKakao_account()
                .getEmail();
        return new OauthSignUpResponse(email);
    }

    // 카카오 프로필 조회
    public KakaoProfile findKakaoProfile(String token) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<Void> kakaoProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> kakaoProfileResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                kakaoProfileRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(kakaoProfileResponse.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            throw new BusinessExceptionHandler(ErrorCode.KAKAO_PROFILE_PARSE_FAILED);
        }
    }
}
