package taskweaver.taskweaver_backend.api.retrospective.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RetrospectiveRequest {
    @Getter
    public static class RetrospectiveCreateRequest {
        @Schema(description = "회고록 제목", example = "회고록 제목1")
        String title;
    }
}
