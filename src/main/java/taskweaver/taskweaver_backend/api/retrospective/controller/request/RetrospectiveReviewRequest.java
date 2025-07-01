package taskweaver.taskweaver_backend.api.retrospective.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taskweaver.taskweaver_backend.domain.retrospective.model.RetrospectiveReviewType;

@Getter
@AllArgsConstructor
public class RetrospectiveReviewRequest {
    @Getter
    @NoArgsConstructor
    public static class ReviewRequest {
        private RetrospectiveReviewType reviewType;
    }
}
