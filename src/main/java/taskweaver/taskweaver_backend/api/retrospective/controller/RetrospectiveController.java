package taskweaver.taskweaver_backend.api.retrospective.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import taskweaver.taskweaver_backend.api.retrospective.controller.request.RetrospectiveRequest;
import taskweaver.taskweaver_backend.api.retrospective.service.RetrospectiveService;
import taskweaver.taskweaver_backend.api.retrospective.service.response.RetrospectiveResponse;
import taskweaver.taskweaver_backend.common.code.ApiResponse;
import taskweaver.taskweaver_backend.common.code.SuccessCode;

@Tag(name = "회고록 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class RetrospectiveController {
    private final RetrospectiveService retrospectiveService;

    @Operation(summary = "회고록 생성", description = "특정 프로젝트에 회고록을 생성하는 api입니다.")
    @PostMapping("/projects/{projectId}/retrospectives")
    public ResponseEntity<ApiResponse<RetrospectiveResponse.RetrospectiveCreateResponse>> createRetrospective(
            @PathVariable Long projectId,
            @RequestBody RetrospectiveRequest.RetrospectiveCreateRequest request,
            @AuthenticationPrincipal User user)
    {
        RetrospectiveResponse.RetrospectiveCreateResponse responseDTO =
                retrospectiveService.createRetrospective(projectId, request, Long.parseLong(user.getUsername()));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.onSuccess(SuccessCode.INSERT_SUCCESS, responseDTO));
    }

    @Operation(summary = "회고록 삭제", description = "회고록을 삭제하는 api입니다.")
    @DeleteMapping("/retrospectives/{retroId}")
    public ApiResponse<?> deleteRetrospective(
            @PathVariable(name = "retroId") Long retroId,
            @AuthenticationPrincipal User user)
    {
        retrospectiveService.deleteRetrospective(retroId, Long.parseLong(user.getUsername()));
        return ApiResponse.onSuccess(SuccessCode.DELETE_SUCCESS);
    }

}
