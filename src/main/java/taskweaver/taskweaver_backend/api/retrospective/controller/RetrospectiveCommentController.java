package taskweaver.taskweaver_backend.api.retrospective.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import taskweaver.taskweaver_backend.api.retrospective.controller.request.RetrospectiveCommentRequest;
import taskweaver.taskweaver_backend.api.retrospective.service.RetrospectiveCommentService;
import taskweaver.taskweaver_backend.api.retrospective.service.response.RetrospectiveCommentResponse;

@Tag(name = "회고록 내용(댓글) API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class RetrospectiveCommentController {

    private final RetrospectiveCommentService retrospectiveCommentService;

    @Operation(summary = "회고록 내용(댓글) 생성", description = "회고록에 내용(댓글)을 생성하는 api입니다.ㅣ")
    @PostMapping("/retrospectives/{retrospectiveId}/comments")
    public ResponseEntity<RetrospectiveCommentResponse.CreateCommentResponse> createRetrospectiveComment(
            @PathVariable Long retrospectiveId,
            @RequestBody RetrospectiveCommentRequest.CreateCommentRequest request,
            @AuthenticationPrincipal User user
    ) {
        RetrospectiveCommentResponse.CreateCommentResponse response = retrospectiveCommentService.createRetrospectiveComment(
                retrospectiveId,
                request,
                Long.parseLong(user.getUsername())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}