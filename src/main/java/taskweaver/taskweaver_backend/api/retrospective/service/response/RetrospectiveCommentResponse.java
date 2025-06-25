package taskweaver.taskweaver_backend.api.retrospective.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class RetrospectiveCommentResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCommentResponse {
        private Long commentId;
        private String content;
        private String writerNickname;
        private Long writerId;
        private Long parentId;
        private LocalDateTime createdAt;

    }
}
