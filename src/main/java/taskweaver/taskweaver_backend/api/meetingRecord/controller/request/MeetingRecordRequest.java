package taskweaver.taskweaver_backend.api.meetingRecord.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetingRecordRequest {
    @Getter
    public static class MeetingCreateRequest {
        @Schema(description = "회의록 제목", example = "회의록1")
        String title;
    }

    @Getter
    public static class MeetingUpdateTitleRequest {
        @Schema(description = "회의록 제목", example = "회의록-수정1")
        String title;
    }

    @Getter
    public static class MeetingUpdateContentRequest {
        @Schema(description = "회의록 내용의 제목", example = "회의록 소제목1")
        String subTitle;

        @Schema(description = "회의록 내용", example = "회의록 내용1")
        String content;
    }
}
