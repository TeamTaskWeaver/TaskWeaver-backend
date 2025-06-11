package taskweaver.taskweaver_backend.api.member.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateAccessTokenRequest(
        @NotBlank
        @Schema(description = "회원의 리프레시 토큰")
        String refreshToken,

        @NotBlank
        @Schema(description = "회원의 만료된 엑세스 토큰")
        String oldAccessToken
) {
}