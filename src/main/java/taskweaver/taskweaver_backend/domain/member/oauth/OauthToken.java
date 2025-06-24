package taskweaver.taskweaver_backend.domain.member.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

@Getter
public class OauthToken {

    @JsonProperty("access_token")
    private String accessToken; // 변수명은 camelCase로 변경하는 것이 좋습니다.

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("refresh_token_expires_in")
    private int refreshTokenExpiresIn;


    private String scope;

    @JsonProperty("id_token")
    private String idToken;

}

