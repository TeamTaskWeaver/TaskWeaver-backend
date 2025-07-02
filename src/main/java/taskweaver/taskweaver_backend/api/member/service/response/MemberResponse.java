package taskweaver.taskweaver_backend.api.member.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taskweaver.taskweaver_backend.domain.member.model.LoginType;

public class MemberResponse {
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class ProfileResponse {
        private Long id;
        private String email;
        private String nickname;
    }
}
