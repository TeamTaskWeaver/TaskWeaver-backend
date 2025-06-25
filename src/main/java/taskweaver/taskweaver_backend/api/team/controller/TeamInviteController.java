package taskweaver.taskweaver_backend.api.team.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import taskweaver.taskweaver_backend.api.team.service.TeamInviteService;
import taskweaver.taskweaver_backend.api.team.service.response.TeamResponse;
import taskweaver.taskweaver_backend.common.code.ApiResponse;
import taskweaver.taskweaver_backend.common.code.SuccessCode;

@Tag(name = "팀원 초대 API")
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/v1/teams/invites")
public class TeamInviteController {

    private final TeamInviteService teamInviteService;
    @Operation(summary = "초대 코드로 팀 정보 조회", description = "팀 생성시 랜덤하게 생성된 inviteCode로 팀을 검색하는 api입니다.")
    @GetMapping("/{inviteCode}")
    public ApiResponse<TeamResponse.TeamInviteInfoResponse> getTeamInfoByInviteCode(
            @PathVariable(name = "inviteCode") String inviteCode) {

        return ApiResponse.onSuccess(
                SuccessCode.SELECT_SUCCESS,
                teamInviteService.getTeamInfoByInviteCode(inviteCode)
        );
    }

    @Operation(summary = "팀 초대 수락", description = "팀 초대를 수락하는 api입니다. 팀 멤버로 자동 등록.")
    @PostMapping(value = "/{inviteCode}/accept")
    public ApiResponse<TeamResponse.TeamJoinSuccessResponse> acceptInvitation(
            @PathVariable(name = "inviteCode") String inviteCode,
            @AuthenticationPrincipal User user) {

        return ApiResponse.onSuccess(
                SuccessCode.INSERT_SUCCESS,
                teamInviteService.acceptInvitation(
                        inviteCode,
                        Long.parseLong(user.getUsername())
                )
        );
    }
}
