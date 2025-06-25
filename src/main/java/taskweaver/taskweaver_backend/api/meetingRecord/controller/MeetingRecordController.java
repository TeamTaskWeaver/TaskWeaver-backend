package taskweaver.taskweaver_backend.api.meetingRecord.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import taskweaver.taskweaver_backend.api.meetingRecord.controller.request.MeetingRecordRequest;
import taskweaver.taskweaver_backend.api.meetingRecord.service.response.MeetingRecordResponse;
import taskweaver.taskweaver_backend.api.meetingRecord.service.MeetingRecordService;
import taskweaver.taskweaver_backend.common.code.ApiResponse;
import taskweaver.taskweaver_backend.common.code.SuccessCode;

@Tag(name = "회의록 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1") // 기본 경로를 /v1으로 변경
public class MeetingRecordController {

    private final MeetingRecordService meetingRecordService;

    @Operation(summary = "회의록 생성", description = "회의록 제목을 등록하는 api입니다.")
    @PostMapping("/projects/{projectId}/meetings")
    public ResponseEntity<ApiResponse<MeetingRecordResponse.MeetingCreateResponse>> createMeetingRecord(
            @PathVariable Long projectId,
            @RequestBody MeetingRecordRequest.MeetingCreateRequest request,
            @AuthenticationPrincipal User user
    ) {

        MeetingRecordResponse.MeetingCreateResponse responseDTO =
                meetingRecordService.createMeetingRecord(projectId, request, Long.parseLong(user.getUsername()));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.onSuccess(SuccessCode.INSERT_SUCCESS, responseDTO));
    }

    @Operation(summary = "회의록 타이틀 수정", description = "회의록 제목을 수정하는 api입니다.")
    @PatchMapping("meetings/{meetingId}")
    public ApiResponse<MeetingRecordResponse.MeetingUpdateTitleResponse> updateMeetingRecordTitle(@PathVariable(name = "meetingId") Long meetingId, @RequestBody MeetingRecordRequest.MeetingUpdateTitleRequest request, @AuthenticationPrincipal User user) {
        MeetingRecordResponse.MeetingUpdateTitleResponse responseDto = meetingRecordService.updateMeetingRecordTitle(
                meetingId,
                request
        );

        return ApiResponse.onSuccess(SuccessCode.UPDATE_SUCCESS, responseDto);
    }

    @Operation(summary = "회의록 내용 등록/수정", description = "회의록 내부 제목과 내용 등록/수정하는 api입니다.")
    @PatchMapping("meetings/contents/{meetingId}")
    public ApiResponse<MeetingRecordResponse.MeetingUpdateContentResponse> updateMeetingRecordContent(@PathVariable(name = "meetingId") Long meetingId, @RequestBody MeetingRecordRequest.MeetingUpdateContentRequest request, @AuthenticationPrincipal User user) {
        MeetingRecordResponse.MeetingUpdateContentResponse responseDto = meetingRecordService.updateMeetingRecordContent(
                meetingId,
                request
        );

        return ApiResponse.onSuccess(SuccessCode.UPDATE_SUCCESS, responseDto);
    }


    @Operation(summary = "회의록 내용 삭제", description = "회의록 내부 제목과 내용을 삭제(nullr값으로 변경)하는 api입니다.")
    @DeleteMapping("meetings/contents/{meetingId}")
    public ApiResponse<MeetingRecordResponse.MeetingUpdateContentResponse> deleteMeetingRecordContent(
            @PathVariable(name = "meetingId") Long meetingId,
            @AuthenticationPrincipal User user) {
        MeetingRecordResponse.MeetingUpdateContentResponse responseDto = meetingRecordService.deleteMeetingRecordContent(
                meetingId
        );

        return ApiResponse.onSuccess(SuccessCode.UPDATE_SUCCESS, responseDto);
    }

    @Operation(summary = "회의록 조회", description = "해당 회의록을 조회하는 api입니다.(아젠다, 회의록 세부 내용 포함)")
    @GetMapping("meetings/{meetingId}")
    public ApiResponse<MeetingRecordResponse.MeetingDetailsResponse> getMeetingDetails(
            @PathVariable Long meetingId,
            @AuthenticationPrincipal User user) {

        MeetingRecordResponse.MeetingDetailsResponse responseDTO = meetingRecordService.getMeetingDetails(meetingId);
        return ApiResponse.onSuccess(SuccessCode.SELECT_SUCCESS, responseDTO);
    }
}