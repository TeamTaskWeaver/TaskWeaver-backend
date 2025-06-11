package taskweaver.taskweaver_backend.api.team.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamRequest {
    @Getter
    public static class teamCreateRequest {
        @Schema(description = "팀 이름", example = "Team Name")
        String name;

        @Schema(description = "팀 소개", example = "Team Description")
        String description;
    }
}
