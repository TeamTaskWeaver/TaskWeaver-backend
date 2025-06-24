package taskweaver.taskweaver_backend.api.meetingRecord.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import taskweaver.taskweaver_backend.api.meetingRecord.controller.request.MeetingRecordRequest;
import taskweaver.taskweaver_backend.api.meetingRecord.service.MeetingRecordResponse;
import taskweaver.taskweaver_backend.api.meetingRecord.service.response.MeetingRecordService;
import taskweaver.taskweaver_backend.common.code.ApiResponse;
import taskweaver.taskweaver_backend.common.code.SuccessCode;

@Tag(name = "회의록 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1") // 기본 경로를 /v1으로 변경
public class MeetingRecordController {

    private final MeetingRecordService meetingRecordService;

    @Operation(summary = "특정 프로젝트에 회의록 생성", description = "URL 경로에 명시된 프로젝트에 새로운 회의록을 생성합니다.")
    @PostMapping("/projects/{projectId}/meeting-records") // URL 경로 변경
    public ResponseEntity<ApiResponse<MeetingRecordResponse.MeetingCreateResponse>> createMeetingRecord(
            @PathVariable Long projectId, // URL 경로에서 projectId를 파라미터로 받음
            @RequestBody MeetingRecordRequest.MeetingCreateRequest request,
            @AuthenticationPrincipal User user
    ) {
        // 서비스 호출 시 projectId를 직접 전달
        MeetingRecordResponse.MeetingCreateResponse responseDTO =
                meetingRecordService.createMeetingRecord(projectId, request, Long.parseLong(user.getUsername()));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.onSuccess(SuccessCode.INSERT_SUCCESS, responseDTO));
    }
}