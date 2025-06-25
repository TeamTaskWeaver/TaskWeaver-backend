package taskweaver.taskweaver_backend.api.team.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import taskweaver.taskweaver_backend.api.team.controller.request.TeamRequest;
import taskweaver.taskweaver_backend.api.team.service.TeamAdminService;
import taskweaver.taskweaver_backend.common.code.ApiResponse;
import taskweaver.taskweaver_backend.common.code.SuccessCode;

@Tag(name = "팀 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/teams/{teamId}")
public class TeamAdminController {

    private final TeamAdminService teamAdminService;

    @PatchMapping("/leader")
    @Operation(summary = "팀장 위임", description = "팀장 변경하는 api입니다.")
    public ApiResponse<?> changeTeamLeader(
                                            @PathVariable Long teamId,
                                            @RequestBody @Valid TeamRequest.ChangeLeaderRequest request,
                                            @AuthenticationPrincipal User user) {
        teamAdminService.changeTeamLeader(
                teamId,
                request.getNewLeaderId(),
                Long.parseLong(user.getUsername())
        );

        return ApiResponse.onSuccess(
                SuccessCode.UPDATE_SUCCESS,
                "팀장 위임이 성공적으로 완료되었습니다."
        );
    }

    @DeleteMapping("/members")
    @Operation(summary = "팀원 내보내기", description = "팀원 삭제하는 api입니다.")
    public ApiResponse<?> deleteTeamMembers(
            @PathVariable Long teamId,
            @RequestBody @Valid TeamRequest.DeleteMembersRequest request,
            @AuthenticationPrincipal User user) {

        teamAdminService.deleteTeamMembers(teamId, request.getMemberIds(), Long.parseLong(user.getUsername()));
        return ApiResponse.onSuccess(
                SuccessCode.DELETE_SUCCESS,
                "팀원 내보내기가 성공적으로 완료되었습니다."
        );
    }
}