// KakaoProfile.java
package taskweaver.taskweaver_backend.domain.member.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class) // snake_case를 camelCase로 자동 매핑
public class KakaoProfile {
    private Long id;
    @JsonProperty("connected_at") // 필요시 명시적 매핑 가능 (JsonNaming 사용시 생략 가능)
    private String connectedAt;

    private KakaoAccount kakaoAccount; // 이 부분이 null이라는 뜻!

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class) // 내부 클래스에도 필요할 수 있음
    public static class KakaoAccount {
        private Boolean profileNeedsAgreement;
        private Profile profile; // 이 안에 닉네임, 프로필 이미지 등
        private Boolean hasEmail;
        //private Boolean emailNeedsAgreement;
        //private Boolean isEmailValid;
        //private Boolean isEmailVerified;
        //private String email; // 우리가 필요한 이메일 필드

        // 기타 필요한 필드 (성별, 연령대, 전화번호 등)
        // private String gender;
        // private String ageRange;
        // ...

        @Data
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class Profile {
            private String nickname;
            private String thumbnailImageUrl;
            private String profileImageUrl;
            private Boolean isDefaultImage;
        }
    }
}