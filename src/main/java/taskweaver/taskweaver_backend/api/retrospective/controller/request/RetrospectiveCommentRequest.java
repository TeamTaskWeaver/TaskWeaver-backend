package taskweaver.taskweaver_backend.api.retrospective.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RetrospectiveCommentRequest {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCommentRequest {
        private String content;
        private Long parentId;

    }

    @Getter
    @NoArgsConstructor
    public static class UpdateCommentRequest {
        private String content;
    }
}
