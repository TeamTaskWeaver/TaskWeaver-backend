package taskweaver.taskweaver_backend.api.team.service.converter;

import org.springframework.stereotype.Component;
import taskweaver.taskweaver_backend.api.team.controller.request.TeamRequest;
import taskweaver.taskweaver_backend.api.team.service.response.TeamResponse;
import taskweaver.taskweaver_backend.domain.member.model.Member;
import taskweaver.taskweaver_backend.domain.team.model.Team;

import java.util.List;
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


    public static String generateInviteLink() {
        UUID uuid = UUID.randomUUID();
        // 도메인 결정 후
        return "https://localhost:" + "8081" + "/invite/" + uuid.toString();
    }


}
