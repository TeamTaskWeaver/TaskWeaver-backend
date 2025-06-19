package taskweaver.taskweaver_backend.api.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taskweaver.taskweaver_backend.common.code.ErrorCode;
import taskweaver.taskweaver_backend.common.exception.handler.BusinessExceptionHandler;
import taskweaver.taskweaver_backend.common.validation.TeamValidator;
import taskweaver.taskweaver_backend.domain.member.repository.MemberRepository;
import taskweaver.taskweaver_backend.domain.team.model.Team;
import taskweaver.taskweaver_backend.domain.team.model.TeamMember;
import taskweaver.taskweaver_backend.domain.team.model.TeamRole;
import taskweaver.taskweaver_backend.domain.team.repository.TeamMemberRepository;
import taskweaver.taskweaver_backend.domain.team.repository.TeamRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamAdminService {
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Transactional
    public void changeTeamLeader(Long teamId, Long newLeaderId, Long currentUserId) {
        Team team = getTeamOrThrow(teamId);

        TeamMember currentLeader = teamMemberRepository.findByTeamIdAndMemberId(teamId, currentUserId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.MEMBER_NOT_FOUND_IN_TEAM));

        TeamValidator.validateTeamLeader(team, currentUserId);

        TeamMember newLeader = teamMemberRepository.findByTeamIdAndMemberId(teamId, newLeaderId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.MEMBER_NOT_FOUND_IN_TEAM));

        if (currentLeader.getId().equals(newLeader.getId())) {
            throw new BusinessExceptionHandler(ErrorCode.CANNOT_APPOINT_SELF_AS_LEADER_AGAIN);
        }

        currentLeader.changeRole(TeamRole.MEMBER);
        newLeader.changeRole(TeamRole.LEADER);

        team.updateTeamLeader(newLeader.getMember());
    }

    private Team getTeamOrThrow(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.TEAM_NOT_FOUND));
    }
}
