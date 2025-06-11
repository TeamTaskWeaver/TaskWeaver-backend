package taskweaver.taskweaver_backend.api.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
거import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import taskweaver.taskweaver_backend.api.member.controller.request.SignInRequest;
import taskweaver.taskweaver_backend.api.member.controller.request.SignUpRequest;
import taskweaver.taskweaver_backend.api.member.service.SignService;
import taskweaver.taskweaver_backend.common.code.ApiResponse;
import taskweaver.taskweaver_backend.common.code.SuccessCode;

@Tag(name = "회원 가입 및 로그인")
@RequiredArgsConstructor
@RestController
@RequestMapping
@Slf4j
@Validated
public class SignController {
    private final SignService signService;

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

    @Operation(summary = "로그인")
    @PostMapping("/v1/auth/sign-in")
    public ResponseEntity<ApiResponse> signIn(@RequestBody SignInRequest request) throws JsonProcessingException {
        ApiResponse ar = ApiResponse.builder()
                .result(signService.signIn(request))
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();
        return new ResponseEntity<>(ar, HttpStatus.OK);
    }

    @GetMapping("/v1/auth/kakao")
    public ResponseEntity<ApiResponse> getLogin(@RequestParam("code") String code) {
        ApiResponse ar = ApiResponse.builder()
                .result(signService.getKakaoAccessToken(code))
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();
        return new ResponseEntity<>(ar, HttpStatus.OK);
    }
}