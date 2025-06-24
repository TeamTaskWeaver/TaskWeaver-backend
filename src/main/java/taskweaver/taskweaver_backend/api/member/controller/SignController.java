package taskweaver.taskweaver_backend.api.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import taskweaver.taskweaver_backend.api.member.controller.request.AccessTokenResponse;
import taskweaver.taskweaver_backend.api.member.controller.request.NicknameUpdateRequest;
import taskweaver.taskweaver_backend.api.member.controller.request.SignInRequest;
import taskweaver.taskweaver_backend.api.member.controller.request.SignUpRequest;

import taskweaver.taskweaver_backend.api.member.service.MemberService;
import taskweaver.taskweaver_backend.api.member.service.SignService;
import taskweaver.taskweaver_backend.api.member.service.response.SignInResponse;
import taskweaver.taskweaver_backend.auth.CookieUtil;
import taskweaver.taskweaver_backend.common.code.ApiResponse;
import taskweaver.taskweaver_backend.common.code.SuccessCode;

import java.io.IOException;

@Tag(name = "회원 가입 및 로그인")
@RequiredArgsConstructor
@RestController
@RequestMapping
@Slf4j
@Validated
public class SignController {
    private final SignService signService;
    private final MemberService memberService;

    @Value("${jwt.refresh-expiration-days}")
    private long refreshTokenExpirationDays;

    @Value("${app.frontend.redirect-uri}") // yml에 설정한 값 주입
    private String frontendRedirectUri;


    private final CookieUtil cookieUtil;
    @Operation(summary = "회원 가입")
    @PostMapping(value = "/v1/auth/sign-up")

    public ResponseEntity<ApiResponse> signUp(@RequestBody SignUpRequest reqeust) {
        ApiResponse ar = ApiResponse.builder()
                .result(signService.registerMember(reqeust))
                .resultCode(SuccessCode.INSERT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.INSERT_SUCCESS.getMessage())
                .build();
        return new ResponseEntity<>(ar, HttpStatus.OK);
    }

//    @Operation(summary = "로그인")
//    @PostMapping("/v1/auth/sign-in-test")
//    public ResponseEntity<ApiResponse> signIn(@RequestBody SignInRequest request) throws JsonProcessingException {
//        ApiResponse ar = ApiResponse.builder()
//                .result(signService.signIn(request))
//                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
//                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage())
//                .build();
//        return new ResponseEntity<>(ar, HttpStatus.OK);
//    }

    @Operation(summary = "로그인")
    @PostMapping("/v1/auth/sign-in")
    public ApiResponse<SignInResponse> signIn(
                                               @RequestBody @Valid SignInRequest request,
                                               HttpServletResponse response) throws JsonProcessingException { // 2. IOException 제거 (필요 시)

        SignInResponse signInResponse = signService.signIn(request);

        long maxAgeSeconds = refreshTokenExpirationDays * 24 * 60 * 60;
        cookieUtil.createRefreshTokenCookie(
                response,
                signInResponse.getRefreshToken(),
                maxAgeSeconds
        );

        return ApiResponse.onSuccess(SuccessCode.SELECT_SUCCESS, signInResponse);
    }


//    @Operation(summary = "카카오 로그인")
//    @GetMapping("/v1/auth/kakao-test")
//    public ResponseEntity<ApiResponse> getLogin(@RequestParam("code") String code) {
//        ApiResponse ar = ApiResponse.builder()
//                .result(signService.getKakaoAccessToken(code))
//                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
//                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage())
//                .build();
//        return new ResponseEntity<>(ar, HttpStatus.OK);
//    }



    @Operation(summary = "카카오 로그인")
    @GetMapping("/v1/auth/kakao")
    public ApiResponse<SignInResponse> kakaoLogin(
                                                   @RequestParam("code") String code,
                                                   HttpServletResponse response) throws JsonProcessingException { // 2. IOException 제거 (필요 시)


        SignInResponse kakaoLoginResponse = signService.getKakaoAccessToken(code);

        long maxAgeSeconds = refreshTokenExpirationDays * 24 * 60 * 60;
        cookieUtil.createRefreshTokenCookie(
                response,
                kakaoLoginResponse.getRefreshToken(),
                maxAgeSeconds
        );

        return ApiResponse.onSuccess(SuccessCode.SELECT_SUCCESS, kakaoLoginResponse);
    }

    @Operation(summary = "닉네임 업데이트")
    @PatchMapping("/v1/auth/nickname")
    public ResponseEntity<ApiResponse> updateNickname(@RequestBody NicknameUpdateRequest request, @AuthenticationPrincipal User user) {

        Long memberId = Long.parseLong(user.getUsername());
        memberService.updateNickname(memberId, request.getNickname());

        ApiResponse ar = ApiResponse.builder()
                .result(null)
                .resultCode(SuccessCode.UPDATE_SUCCESS.getStatus())
                .resultMsg(SuccessCode.UPDATE_SUCCESS.getMessage())
                .build();
        return new ResponseEntity<>(ar, HttpStatus.OK);
    }

    @Operation(summary = "Access Token 재발급", description = "RefreshToken(쿠키)을 이용하여 새로운 AccessToken을 발급합니다.")
    @PostMapping("/v1/auth/refresh")
    public ApiResponse<AccessTokenResponse> reissueAccessToken(
            @CookieValue(name = "refreshToken") String refreshToken, // 쿠키에서 RefreshToken 가져오기
            HttpServletResponse response) {

        // 서비스 레이어에 토큰 재발급 요청
        AccessTokenResponse accessTokenResponse = signService.reissueAccessToken(refreshToken, response);

        return ApiResponse.onSuccess(SuccessCode.TOKEN_REISSUED, accessTokenResponse);
    }

}