package taskweaver.taskweaver_backend.api.retrospective.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import taskweaver.taskweaver_backend.api.retrospective.controller.request.RetrospectiveReviewRequest;
import taskweaver.taskweaver_backend.api.retrospective.service.converter.RetrospectiveReviewService;
import taskweaver.taskweaver_backend.api.retrospective.service.response.RetrospectiveReviewResponse;

@Tag(name = "회고록 리뷰 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class RetrospectiveReviewController {

    private final RetrospectiveReviewService reviewService;

    @Operation(summary = "회고록 리뷰 체크", description = "회고록에 리뷰(LIKE/DISLIKE) 체크하는 API입니다.")
    @PostMapping("/retrospectives/{retroId}/review")
    public ResponseEntity<RetrospectiveReviewResponse.ReviewResponse> review(
            @PathVariable Long retroId,
            @RequestBody RetrospectiveReviewRequest.ReviewRequest request,
            @AuthenticationPrincipal User user
    ) {
        RetrospectiveReviewResponse.ReviewResponse response = reviewService.createReview(
                retroId,
                request,
                Long.parseLong(user.getUsername())
        );
        return ResponseEntity.ok(response);
    }
}
