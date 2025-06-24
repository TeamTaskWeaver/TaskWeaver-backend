package taskweaver.taskweaver_backend.api.meetingRecord.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AgendaResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateAgendaResponse {
        @Schema(description = "생성된 아젠다의 ID", example = "10")
        private Long agendaId;
        @Schema(description = "생성된 아젠다의 제목", example = "서버 아키텍처 v2 논의")
        private String title;
    }
}
