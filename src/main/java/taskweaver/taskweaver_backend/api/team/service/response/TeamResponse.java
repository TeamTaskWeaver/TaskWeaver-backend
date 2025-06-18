package taskweaver.taskweaver_backend.api.team.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
    public static class TeamCreateResponse {
        Long id;
        String name;
        String description;
        String inviteLink;
        Long teamLeaderId;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamListResponse {

        @Schema(description = "팀 id", example = "1")
        Long id;

        @Schema(description = "팀 이름", example = "팀 이름")
        String name;

        @Schema(description = "팀 소개", example = "팀 소개")
        String description;

        @Schema(description = "생성 날짜", example = "2024-02-08T22:58:10.061223")
        LocalDateTime createdAt;

        @Schema(description = "로그인한 유저 권한", example = "LEADER")
        String myRole;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamUpdateResponse {
        Long id;
        String name;
        String description;
        String inviteLink;
        Long teamLeader;
        LocalDateTime updateAt;
    }
}
