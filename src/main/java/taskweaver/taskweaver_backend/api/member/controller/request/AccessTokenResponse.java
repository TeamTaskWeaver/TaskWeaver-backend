package taskweaver.taskweaver_backend.api.member.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenResponse {
    @Schema(description = "새로 발급된 엑세스 토큰", example = "newAccessToken1")
    private String accessToken;
}