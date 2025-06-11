package taskweaver.taskweaver_backend.api.member.service.response;


import io.swagger.v3.oas.annotations.media.Schema;
import taskweaver.taskweaver_backend.domain.member.model.LoginType;

public record SignInResponse(
        @Schema(description = "회원 아이디", example = "1")
        Long id,
        @Schema(description = "회원 이메일", example = "xxx@naver.com")
        String email,
        @Schema(description = "로그인 유형", example = "DEFAULT")
        LoginType type,

        @Schema(description = "닉네임", example = "conan")
        String nickname,

        @Schema(description = "엑세스 토큰", example = "accessToken")
        String accessToken,
        @Schema(description = "리프레시 토큰", example = "refreshToken")
        String refreshToken
) {
}
