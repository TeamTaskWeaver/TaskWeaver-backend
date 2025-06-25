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

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamAdminService {
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamMemberManager teamMemberManager;

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





    @Transactional
    public void deleteTeamMembers(Long teamId, List<Long> memberIds, Long currentUserId) {
        // 1. 리더 권한 확인 (기존과 동일)
        TeamMember leader = teamMemberRepository.findByTeamIdAndMemberId(teamId, currentUserId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.FORBIDDEN_ACCESS));

        if (leader.getRole() != TeamRole.LEADER) {
            throw new BusinessExceptionHandler(ErrorCode.FORBIDDEN_ACCESS);
        }

        // 2. 기본 유효성 검증 (기존과 동일)
        if (memberIds == null || memberIds.isEmpty()) {
            throw new BusinessExceptionHandler(ErrorCode.EMPTY_MEMBER_LIST_TO_REMOVE);
        }
        if (memberIds.contains(currentUserId)) {
            throw new BusinessExceptionHandler(ErrorCode.CANNOT_REMOVE_LEADER);
        }

        // 3. 삭제 대상 TeamMember 엔티티들 조회 (기존과 동일)
        List<TeamMember> membersToDelete = teamMemberRepository.findAllByTeamIdAndMemberIdIn(teamId, memberIds);

        // 4. [핵심] 요청된 ID 개수와 실제 조회된 엔티티 개수가 같은지 확인
        if (membersToDelete.size() != memberIds.size()) {
            // 요청된 ID 중에 유효하지 않은 ID가 하나 이상 포함되어 있다는 의미
            throw new BusinessExceptionHandler(ErrorCode.MEMBER_NOT_FOUND_IN_TEAM);
        }

        // 5. 각 멤버를 소프트 삭제 (기존과 동일)
        membersToDelete.forEach(TeamMember::deleteSoftly);
    }

    private Team getTeamOrThrow(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.TEAM_NOT_FOUND));
    }

}
