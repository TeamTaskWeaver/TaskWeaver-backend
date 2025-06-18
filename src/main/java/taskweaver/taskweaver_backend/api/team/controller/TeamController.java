package taskweaver.taskweaver_backend.api.team.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import taskweaver.taskweaver_backend.api.team.controller.request.TeamRequest;
import taskweaver.taskweaver_backend.api.team.service.TeamInviteService;
import taskweaver.taskweaver_backend.api.team.service.TeamService;
import taskweaver.taskweaver_backend.api.team.service.response.TeamResponse;
import taskweaver.taskweaver_backend.common.code.ApiResponse;
import taskweaver.taskweaver_backend.common.code.SuccessCode;


@Tag(name = "팀 관련")
@RequiredArgsConstructor
@Slf4j
@RestController
public class TeamController {

    private final TeamService teamService;
    private final TeamInviteService teamInviteService;

    @Operation(summary = "팀 생성")
    @PostMapping("v1/teams")
    public ResponseEntity<ApiResponse<TeamResponse.TeamCreateResponse>> createTeam(@RequestBody TeamRequest.TeamCreateRequest request, @AuthenticationPrincipal User user) {
        ApiResponse apiResponse = ApiResponse.builder()
                .result(teamService.createTeam(request, Long.parseLong(user.getUsername())))
                .resultCode(SuccessCode.INSERT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.INSERT_SUCCESS.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @Operation(summary =  "로그인한 유저의 팀 전체 조회")
    @GetMapping("v1/teams")
    public ResponseEntity<ApiResponse<TeamResponse.TeamListResponse>> AllTeam(@AuthenticationPrincipal User user) {
        ApiResponse apiResponse = ApiResponse.builder()
                .result(teamService.getMyTeams(Long.parseLong(user.getUsername())))
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "팀 삭제")
    @DeleteMapping("v1/teams/{teamId}")
    public ResponseEntity<ApiResponse> deleteTeam(@PathVariable(name = "teamId") Long teamId, @AuthenticationPrincipal User user) {
        teamService.deleteTeam(teamId, Long.parseLong(user.getUsername()));
        ApiResponse apiResponse = ApiResponse.builder()
                .resultCode(SuccessCode.DELETE_SUCCESS.getStatus())
                .resultMsg(SuccessCode.DELETE_SUCCESS.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "팀 수정")
    @PatchMapping("v1/teams/{teamId}")
    public ResponseEntity<ApiResponse<TeamResponse.TeamUpdateResponse>> updateTeam(@PathVariable(name = "teamId") Long teamId, @RequestBody TeamRequest.TeamUpdateRequest request, @AuthenticationPrincipal User user) {
        ApiResponse apiResponse = ApiResponse.builder()
                .result(teamService.updateTeam(teamId, request, Long.parseLong(user.getUsername())))
                .resultCode(SuccessCode.UPDATE_SUCCESS.getStatus())
                .resultMsg(SuccessCode.UPDATE_SUCCESS.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "초대 링크로 팀 정보 조회")
    @GetMapping("v1/teams/invites/{inviteLink}")
    public ResponseEntity<ApiResponse<TeamResponse.TeamInviteInfoResponse>> getTeamInfo(
            @PathVariable(name = "inviteLink") String inviteLink) {

        TeamResponse.TeamInviteInfoResponse responseDto = teamInviteService.getTeamInfoByInviteCode(inviteLink);
        ApiResponse apiResponse = ApiResponse.builder()
                .result(responseDto)
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}