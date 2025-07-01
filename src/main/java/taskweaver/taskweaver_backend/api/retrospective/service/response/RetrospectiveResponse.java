package taskweaver.taskweaver_backend.api.retrospective.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taskweaver.taskweaver_backend.domain.retrospective.model.RetrospectiveReviewType;

import java.time.LocalDateTime;
import java.util.List;

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

    @Getter
    @Builder
    public static class RetrospectiveDetailResponse {
        private Long retrospectiveId;
        private String title;
        private String writerNickname;
        private LocalDateTime createdAt;
        private ReviewInfo review;
        private List<CommentInfo> comments;
    }

    @Getter
    @Builder
    public static class ReviewInfo {
        private long likeCount;
        private long dislikeCount;
        private RetrospectiveReviewType myReviewType;
    }

    @Getter
    @Builder
    public static class CommentInfo {
        private Long commentId;
        private String content;
        private String writerNickname;
        private Long writerId;
        private LocalDateTime createdAt;
        private boolean isDeleted;
        private List<CommentInfo> children; 
    }
}
