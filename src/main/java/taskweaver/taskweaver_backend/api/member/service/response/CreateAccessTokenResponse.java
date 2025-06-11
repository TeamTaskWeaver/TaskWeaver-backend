package taskweaver.taskweaver_backend.api.member.service.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateAccessTokenResponse(
        @Schema(description = "회원의 새로운 엑세스 토큰")
        String newAccessToken
) {
}
