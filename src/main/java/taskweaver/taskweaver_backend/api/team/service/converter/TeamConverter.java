package taskweaver.taskweaver_backend.api.team.service.converter;

import org.springframework.stereotype.Component;
import taskweaver.taskweaver_backend.api.team.controller.request.TeamRequest;
import taskweaver.taskweaver_backend.api.team.service.response.TeamResponse;
import taskweaver.taskweaver_backend.domain.member.model.Member;
import taskweaver.taskweaver_backend.domain.team.model.Team;
import taskweaver.taskweaver_backend.domain.team.model.TeamRole;

import java.util.UUID;
@Component
public class TeamConverter {
    public static Team toCreateRequest(TeamRequest.TeamCreateRequest request, Member teamLeader) {
        return Team.builder()
                .name(request.getName())
                .description(request.getDescription())
                .inviteLink(generateInviteLink())
                .teamLeader(teamLeader)
                .build();
    }


    public static TeamResponse.TeamCreateResponse toCreateResponse(Team team) {
        return new TeamResponse.TeamCreateResponse(
                team.getId(),
                team.getName(),
                team.getDescription(),
                team.getInviteLink(),
                team.getTeamLeader().getId(),
                team.getCreatedAt()
        );
    }

    public static TeamResponse.TeamListResponse toListResponse(Team team, String myRole) {
        return new TeamResponse.TeamListResponse(
                team.getId(),
                team.getName(),
                team.getDescription(),
                team.getCreatedAt(),
                myRole
        );
    }

    public static TeamResponse.TeamUpdateResponse toUpdateResponse(Team team) {
        return new TeamResponse.TeamUpdateResponse(
                team.getId(),
                team.getName(),
                team.getDescription(),
                team.getInviteLink(),
                team.getTeamLeader().getId(),
                team.getModifiedAt()
        );
    }

    public static TeamResponse.TeamInviteInfoResponse toTeamInviteInfoResponse(Team team) {
        return new TeamResponse.TeamInviteInfoResponse(
                team.getId(),
                team.getName()
        );
    }

    public static TeamResponse.TeamJoinSuccessResponse toTeamJoinSuccessResponse(Team team, Member member, TeamRole memberRole) {
        return new TeamResponse.TeamJoinSuccessResponse(
                team.getId(),
                member.getId(),
                team.getName(),
                memberRole.toString()
        );
    }



    public static String generateInviteLink() {
        UUID uuid = UUID.randomUUID();
        // 도메인 결정 후
        return uuid.toString();
    }


}
