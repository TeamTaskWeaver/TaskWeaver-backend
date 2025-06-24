package taskweaver.taskweaver_backend.api.meetingRecord.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class AgendaRequest {
    @Getter
    public static class CreateAgendaRequest {
        @Schema(description = "생성할 아젠다의 제목", example = "서버 아키텍처 v2 논의")
        @NotBlank(message = "아젠다 제목은 필수입니다.")
        private String title;
    }
}
