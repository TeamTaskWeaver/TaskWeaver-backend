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


@Component
@RequiredArgsConstructor
public class TeamMemberManager {

    private final TeamMemberRepository teamMemberRepository;
    private final MemberRepository memberRepository;

    public void addLeaderToTeam(Team team, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.TEAM_MEMBER_NOT_FOUND));
        TeamMember teamMember = TeamMember.builder()
                .team(team)
                .member(member)
                .role(TeamRole.LEADER)
                .build();
        teamMemberRepository.save(teamMember);
    }


}

