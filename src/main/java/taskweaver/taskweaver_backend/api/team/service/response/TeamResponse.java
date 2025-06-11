package taskweaver.taskweaver_backend.api.team.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class TeamResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class teamCreateResponse {
        Long id;
        String name;
        String description;
        String inviteLink;
        Long teamLeaderId;
        LocalDateTime createdAt;
    }
}
