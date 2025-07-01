package taskweaver.taskweaver_backend.api.retrospective.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taskweaver.taskweaver_backend.domain.retrospective.model.RetrospectiveReviewType;

public class RetrospectiveReviewResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewResponse {
        private long likeCount;
        private long dislikeCount;
        private RetrospectiveReviewType myReviewType; // 내가 현재 선택한 리뷰 타입

    }
}
