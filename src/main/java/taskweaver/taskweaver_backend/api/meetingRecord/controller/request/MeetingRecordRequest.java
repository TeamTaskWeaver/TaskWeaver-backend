package taskweaver.taskweaver_backend.api.meetingRecord.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetingRecordRequest {
    @Getter
    public static class MeetingCreateRequest {
        @Schema(description = "회의록 제목", example = "Meeting Name")
        String title;
    }
}
