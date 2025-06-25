package taskweaver.taskweaver_backend.api.member.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taskweaver.taskweaver_backend.api.member.controller.request.CreateAccessTokenRequest;
import taskweaver.taskweaver_backend.api.member.service.converter.MemberConverter;
import taskweaver.taskweaver_backend.api.member.service.response.CreateAccessTokenResponse;
import taskweaver.taskweaver_backend.auth.TokenProvider;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;

    @Transactional
    public CreateAccessTokenResponse createNewAccessToken(CreateAccessTokenRequest request) throws JsonProcessingException {
        tokenProvider.validateRefreshToken(request.refreshToken(), request.oldAccessToken());
        String accessToken = tokenProvider.recreateAccessToken(request.oldAccessToken());
        return MemberConverter.toCreateAccessTokenResponse(accessToken);
    }
}
