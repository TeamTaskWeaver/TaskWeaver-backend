package taskweaver.taskweaver_backend.api.meetingRecord.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import taskweaver.taskweaver_backend.api.meetingRecord.controller.request.AgendaRequest;
import taskweaver.taskweaver_backend.api.meetingRecord.service.AgendaService;
import taskweaver.taskweaver_backend.api.meetingRecord.service.response.AgendaResponse;
import taskweaver.taskweaver_backend.common.code.ApiResponse;
import taskweaver.taskweaver_backend.common.code.SuccessCode;

@Tag(name = "회의록-아젠다 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class AgendaController {

    private final AgendaService agendaService;

    @Operation(summary = "아젠다 등록", description = "특정 회의록에 새로운 아젠다를 등록하는 api입니다.")
    @PostMapping("/meetings/{meetingId}/agendas")
    public ApiResponse<AgendaResponse.CreateAgendaResponse> createAgenda(
            @PathVariable Long meetingId,
            @RequestBody AgendaRequest.CreateAgendaRequest request,
            @AuthenticationPrincipal User user) {

        AgendaResponse.CreateAgendaResponse responseDTO = agendaService.createAgenda(meetingId, request);
        return ApiResponse.onSuccess(SuccessCode.INSERT_SUCCESS, responseDTO);
    }

    @Operation(summary = "아젠다 삭제", description = "특정 아젠다를 삭제하는 api입니다.")
    @DeleteMapping("/agendas/{agendaId}")
    public ApiResponse<?> deleteAgenda(
            @PathVariable Long agendaId,
            @AuthenticationPrincipal User user) {

        agendaService.deleteAgenda(agendaId);
        return ApiResponse.onSuccess(SuccessCode.DELETE_SUCCESS, null);
    }
}