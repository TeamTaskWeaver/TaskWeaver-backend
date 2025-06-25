package taskweaver.taskweaver_backend.api.team.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class TeamRequest {
    @Getter
    public static class TeamCreateRequest {
        @Schema(description = "팀 이름", example = "팀 이름1")
        String name;

        @Schema(description = "팀 소개", example = "팀 소개1")
        String description;
    }

    @Getter
    public static class TeamUpdateRequest {
        @Schema(description = "팀 이름", example = "팀 이름1-수정")
        String name;

        @Schema(description = "팀 소개", example = "팀 소개1-수정")
        String description;
    }

    @Getter
    @NoArgsConstructor
    public static class ChangeLeaderRequest {
        private Long newLeaderId;
    }

    @Getter
    @NoArgsConstructor
    public static class DeleteMembersRequest {
        private List<Long> memberIds;
    }
}
