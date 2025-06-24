package taskweaver.taskweaver_backend.api.meetingRecord.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class  MeetingRecordResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MeetingCreateResponse {
        Long id;
        Long userId;
        String title;
        LocalDateTime createdAt;
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MeetingUpdateTitleResponse {
        Long id;
        String title;
        LocalDateTime modifiedAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MeetingUpdateContentResponse {
        Long id;
        String subTitle;
        String content;
        LocalDateTime modifiedAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MeetingDetailsResponse {
        @Schema(description = "회의록 ID")
        private Long meetingId;

        @Schema(description = "회의록 전체 제목")
        private String title;

        @Schema(description = "회의록 소제목")
        private String subTitle;

        @Schema(description = "회의록 전체 내용")
        private String content;

        @Schema(description = "생성 일시")
        private LocalDateTime createdAt;

        @Schema(description = "아젠다 목록")
        private List<AgendaInfo> agendas;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgendaInfo {
        private Long agendaId;
        private String title;
    }
}
