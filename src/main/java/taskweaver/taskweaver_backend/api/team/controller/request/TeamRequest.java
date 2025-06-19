package taskweaver.taskweaver_backend.api.team.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class TeamRequest {
    @Getter
    public static class TeamCreateRequest {
        @Schema(description = "팀 이름", example = "Team Name")
        String name;

        @Schema(description = "팀 소개", example = "Team Description")
        String description;
    }

    @Getter
    public static class TeamUpdateRequest {
        @Schema(description = "팀 이름", example = "Team Name")
        String name;

        @Schema(description = "팀 소개", example = "Team Description")
        String description;
    }

    @Getter
    @NoArgsConstructor
    public static class ChangeLeaderRequest {
        private Long newLeaderId;
    }

}
