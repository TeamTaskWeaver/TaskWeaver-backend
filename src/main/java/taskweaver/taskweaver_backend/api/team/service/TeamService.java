package taskweaver.taskweaver_backend.api.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taskweaver.taskweaver_backend.api.team.controller.request.TeamRequest;
import taskweaver.taskweaver_backend.api.team.service.converter.TeamConverter;
import taskweaver.taskweaver_backend.api.team.service.response.TeamResponse;
import taskweaver.taskweaver_backend.common.code.ErrorCode;
import taskweaver.taskweaver_backend.common.exception.handler.BusinessExceptionHandler;
import taskweaver.taskweaver_backend.common.validation.TeamValidator;
import taskweaver.taskweaver_backend.domain.member.model.Member;
import taskweaver.taskweaver_backend.domain.member.repository.MemberRepository;
import taskweaver.taskweaver_backend.domain.team.model.Team;
import taskweaver.taskweaver_backend.domain.team.model.TeamMember;
import taskweaver.taskweaver_backend.domain.team.repository.TeamRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamMemberManager teamMemberManager;
    private final MemberRepository memberRepository;


    @Transactional
    public TeamResponse.TeamCreateResponse createTeam(TeamRequest.TeamCreateRequest request, Long userId) {

        Member leader = memberRepository.findById(userId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.MEMBER_NOT_FOUND));

        Team team = TeamConverter.toCreateRequest(request, leader);

        team = teamRepository.save(team);
        teamMemberManager.addLeaderToTeam(team, leader);
        return TeamConverter.toCreateResponse(team);
    }

    public List<TeamResponse.TeamListResponse> getMyTeams(Long currentUserId) {
        Member leader = memberRepository.findById(currentUserId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.MEMBER_NOT_FOUND));

        List<TeamMember> userTeams = teamMemberManager.getMembershipsForUser(leader.getId());


        return userTeams.stream()
                .map(userTeam -> TeamConverter.toListResponse(userTeam.getTeam(), userTeam.getRole().name()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteTeam(Long teamId, Long userId) {
        Team team = getTeamOrThrow(teamId);
        TeamValidator.validateTeamLeader(team, userId);
        team.deleteSoftly();
    }

    @Transactional
    public TeamResponse.TeamUpdateResponse updateTeam(Long teamId, TeamRequest.TeamUpdateRequest request, Long userId) {
        Team team = getTeamOrThrow(teamId);
        TeamValidator.validateTeamLeader(team, userId);

        team.updateTeamInfo(request.getName(), request.getDescription());

        teamRepository.save(team);
        return TeamConverter.toUpdateResponse(team);
    }

    private Team getTeamOrThrow(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.TEAM_NOT_FOUND));
    }


}
