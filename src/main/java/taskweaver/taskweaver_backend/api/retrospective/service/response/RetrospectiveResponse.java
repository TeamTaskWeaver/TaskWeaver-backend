package taskweaver.taskweaver_backend.api.retrospective.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class RetrospectiveResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RetrospectiveCreateResponse {
        Long id;
        Long userId;
        String title;
        LocalDateTime createdAt;
    }
}
