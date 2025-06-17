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
import taskweaver.taskweaver_backend.domain.team.model.TeamMember;
import taskweaver.taskweaver_backend.domain.team.repository.TeamMemberRepository;
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
    private final TeamMemberRepository teamMemberRepository;

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
        // 1. 현재 유저 ID를 기반으로 UserTeam 관계와 해당 Team 엔티티를 함께 조회합니다.
        //    (N+1 문제를 방지하기 위해 JOIN FETCH를 사용)
        List<TeamMember> userTeams = teamMemberRepository.findByUserIdWithTeam(currentUserId);

        // 2. 조회된 UserTeam 리스트를 스트림 API를 사용하여 TeamListResponse DTO로 변환합니다.
        //    이 과정에서 각 UserTeam 객체로부터 Team 정보와 해당 유저의 Role 정보를 추출합니다.
        return userTeams.stream()
                .map(userTeam -> TeamConverter.toListResponse(userTeam.getTeam(), userTeam.getRole().name()))
                // userTeam.getRole()이 Enum이라면 .name()으로 String 변환
                // userTeam.getRole()이 String이라면 그대로 사용
                .collect(Collectors.toList());
    }
}
