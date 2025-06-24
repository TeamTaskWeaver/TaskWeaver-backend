package taskweaver.taskweaver_backend.api.meetingRecord.service;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

}
