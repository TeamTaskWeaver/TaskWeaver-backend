package taskweaver.taskweaver_backend.api.team.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import taskweaver.taskweaver_backend.api.team.controller.request.TeamRequest;
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

    @Operation(summary = "팀 생성")
    @PostMapping("/teams")
    public ResponseEntity<ApiResponse<TeamResponse.TeamCreateResponse>> createTeam(@RequestBody TeamRequest.TeamCreateRequest request, @AuthenticationPrincipal User user) {
        try {
            ApiResponse apiResponse = ApiResponse.builder()
                    .result(teamService.createTeam(request, Long.parseLong(user.getUsername())))
                    .resultCode(SuccessCode.INSERT_SUCCESS.getStatus())
                    .resultMsg(SuccessCode.INSERT_SUCCESS.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary =  "로그인한 유저의 팀 전체 조회")
    @GetMapping("/teams")
    public ResponseEntity<ApiResponse<TeamResponse.TeamListResponse>> AllTeam(@AuthenticationPrincipal User user) {
        ApiResponse apiResponse = ApiResponse.builder()
                .result(teamService.getMyTeams(Long.parseLong(user.getUsername())))
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}