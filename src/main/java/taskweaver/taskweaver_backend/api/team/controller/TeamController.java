package taskweaver.taskweaver_backend.api.team.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import taskweaver.taskweaver_backend.api.team.controller.request.TeamRequest;
import taskweaver.taskweaver_backend.api.team.service.TeamAdminService;
import taskweaver.taskweaver_backend.api.team.service.TeamInviteService;
import taskweaver.taskweaver_backend.api.team.service.TeamService;
import taskweaver.taskweaver_backend.api.team.service.response.TeamResponse;
import taskweaver.taskweaver_backend.common.code.ApiResponse;
import taskweaver.taskweaver_backend.common.code.SuccessCode;

import java.util.List;


@Tag(name = "팀 API")
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/v1/teams")
public class TeamController {

    private final TeamService teamService;
    private final TeamInviteService teamInviteService;
    private final TeamAdminService teamAdminService;

    @Operation(summary = "팀 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<TeamResponse.TeamCreateResponse>> createTeam(@RequestBody TeamRequest.TeamCreateRequest request, @AuthenticationPrincipal User user) {
        TeamResponse.TeamCreateResponse data = teamService.createTeam(request, Long.parseLong(user.getUsername()));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.onSuccess(SuccessCode.INSERT_SUCCESS, data));
    }

    @Operation(summary =  "로그인한 유저의 팀 전체 조회")
    @GetMapping
    public ApiResponse<List<TeamResponse.TeamListResponse>> getMyTeams(
            @AuthenticationPrincipal User user) {

        return ApiResponse.onSuccess(
                SuccessCode.SELECT_SUCCESS,
                teamService.getMyTeams(Long.parseLong(user.getUsername()))
        );
    }

    @Operation(summary = "팀 삭제")
    @DeleteMapping("/{teamId}")
    public ApiResponse<?> deleteTeam(@PathVariable(name = "teamId") Long teamId, @AuthenticationPrincipal User user) {
        teamService.deleteTeam(teamId, Long.parseLong(user.getUsername()));
        return ApiResponse.onSuccess(SuccessCode.DELETE_SUCCESS);
    }

    @Operation(summary = "팀 수정")
    @PatchMapping("/{teamId}")
    public ApiResponse<TeamResponse.TeamUpdateResponse> updateTeam(@PathVariable(name = "teamId") Long teamId, @RequestBody TeamRequest.TeamUpdateRequest request, @AuthenticationPrincipal User user) {
        TeamResponse.TeamUpdateResponse responseDto = teamService.updateTeam(
                teamId,
                request,
                Long.parseLong(user.getUsername())
        );

        return ApiResponse.onSuccess(SuccessCode.UPDATE_SUCCESS, responseDto);
    }


    @Operation(summary = "팀 멤버 전체 조회")
    @GetMapping("/{teamId}/members")
    public ApiResponse<TeamResponse.TeamMemberListResponse> getTeamMembers(
            @PathVariable(name = "teamId") Long teamId,
            @AuthenticationPrincipal User user) {

        return ApiResponse.onSuccess(
                SuccessCode.SELECT_SUCCESS,
                teamService.getTeamMembers(teamId, Long.parseLong(user.getUsername()))
        );
    }



    @PatchMapping("v1/teams/{teamId}/leader")
    @Operation(summary = "팀장 위임")
    public ResponseEntity<ApiResponse<Object>> changeTeamLeader(
            @PathVariable Long teamId,
            @RequestBody @Valid TeamRequest.ChangeLeaderRequest request,
            @AuthenticationPrincipal User user) {
        teamAdminService.changeTeamLeader(
                teamId,
                request.getNewLeaderId(),
                Long.parseLong(user.getUsername())
        );
        ApiResponse apiResponse = ApiResponse.builder()
                .resultCode(SuccessCode.UPDATE_SUCCESS.getStatus())
                .resultMsg("팀장 위임이 성공적으로 완료되었습니다.")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}