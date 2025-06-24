package taskweaver.taskweaver_backend.api.meetingRecord.service;

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
    public static class MeetingUpdateResponse {
        Long id;
        String title;
        LocalDateTime modifiedAt;
    }



}
