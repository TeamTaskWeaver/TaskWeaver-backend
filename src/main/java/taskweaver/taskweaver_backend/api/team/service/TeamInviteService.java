package taskweaver.taskweaver_backend.api.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taskweaver.taskweaver_backend.api.team.service.converter.TeamConverter;
import taskweaver.taskweaver_backend.api.team.service.response.TeamResponse;
import taskweaver.taskweaver_backend.common.code.ErrorCode;
import taskweaver.taskweaver_backend.common.exception.handler.BusinessExceptionHandler;
import taskweaver.taskweaver_backend.domain.member.repository.MemberRepository;
import taskweaver.taskweaver_backend.domain.team.model.Team;
import taskweaver.taskweaver_backend.domain.team.repository.TeamMemberRepository;
import taskweaver.taskweaver_backend.domain.team.repository.TeamRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamInviteService {

    private final TeamRepository teamRepository;

    public TeamResponse.TeamInviteInfoResponse getTeamInfoByInviteCode(String inviteLink) {
        Team team = teamRepository.findByInviteLink(inviteLink)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.TEAM_INVITE_LINK_NOT_FOUND));

        return TeamConverter.toTeamInviteInfoResponse(team);
    }
}
