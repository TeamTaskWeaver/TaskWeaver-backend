package taskweaver.taskweaver_backend.api.team.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
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
}

