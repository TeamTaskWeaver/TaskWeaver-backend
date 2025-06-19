package taskweaver.taskweaver_backend.api.team.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import taskweaver.taskweaver_backend.common.code.ErrorCode;
import taskweaver.taskweaver_backend.common.exception.handler.BusinessExceptionHandler;
import taskweaver.taskweaver_backend.domain.member.model.Member;
import taskweaver.taskweaver_backend.domain.member.repository.MemberRepository;
import taskweaver.taskweaver_backend.domain.team.model.Team;
import taskweaver.taskweaver_backend.domain.team.model.TeamMember;
import taskweaver.taskweaver_backend.domain.team.model.TeamRole;
import taskweaver.taskweaver_backend.domain.team.repository.TeamMemberRepository;

import java.util.List;


@Component
@RequiredArgsConstructor
public class TeamMemberManager {

    private final TeamMemberRepository teamMemberRepository;

    public void addLeaderToTeam(Team team, Member leader) {
        TeamMember tm = TeamMember.builder()
                .team(team)
                .member(leader)
                .role(TeamRole.LEADER)
                .build();
        teamMemberRepository.save(tm);
    }

    public List<TeamMember> getMembershipsForUser(Long userId) {

        return teamMemberRepository.findByUserIdWithTeam(userId);
    }

    public List<TeamMember> findTeamMembersByTeamId(Long teamId) {
        // N+1 문제 해결을 위한 fetch join 쿼리 호출
        return teamMemberRepository.findAllByTeamIdWithMember(teamId);
    }

    public boolean isTeamMember(Long teamId, Long memberId) {
        return teamMemberRepository.existsByTeamIdAndMemberId(teamId, memberId);
    }

    @Transactional
    public TeamMember joinTeamAsMember(Team team, Member member) {
        if (teamMemberRepository.existsByTeamAndMember(team, member)) {
            throw new BusinessExceptionHandler(ErrorCode.ALREADY_TEAM_MEMBER);
        }

        TeamMember newTeamMember = TeamMember.builder()
                .team(team)
                .member(member)
                .role(TeamRole.MEMBER)
                .build();

        return teamMemberRepository.save(newTeamMember);
    }
}

