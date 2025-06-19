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
    @Operation(summary = "팀장 위임")
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
}