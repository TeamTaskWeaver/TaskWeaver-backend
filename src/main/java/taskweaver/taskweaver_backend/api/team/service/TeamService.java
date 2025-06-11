package taskweaver.taskweaver_backend.api.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taskweaver.taskweaver_backend.api.team.controller.request.TeamRequest;
import taskweaver.taskweaver_backend.api.team.service.converter.TeamConverter;
import taskweaver.taskweaver_backend.api.team.service.response.TeamResponse;
import taskweaver.taskweaver_backend.common.code.ErrorCode;
import taskweaver.taskweaver_backend.common.exception.handler.BusinessExceptionHandler;
import taskweaver.taskweaver_backend.domain.member.model.Member;
import taskweaver.taskweaver_backend.domain.member.repository.MemberRepository;
import taskweaver.taskweaver_backend.domain.team.model.Team;
import taskweaver.taskweaver_backend.domain.team.repository.TeamRepository;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamMemberManager teamMemberManager;
    private final MemberRepository memberRepository;


    @Transactional
    public TeamResponse.teamCreateResponse createTeam(TeamRequest.teamCreateRequest request, Long userId) {

        Member leader = memberRepository.findById(userId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.MEMBER_NOT_FOUND));

        Team team = TeamConverter.toCreateRequest(request, leader);

        team = teamRepository.save(team);
        teamMemberManager.addLeaderToTeam(team, userId);
        return TeamConverter.toCreateResponse(team);
    }


    private String generateInviteLink() {
        return "https://example.com/invite/" + UUID.randomUUID().toString();
    }


}
