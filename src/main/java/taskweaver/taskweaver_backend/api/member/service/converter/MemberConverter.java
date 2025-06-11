package taskweaver.taskweaver_backend.api.member.service.converter;

import org.springframework.security.crypto.password.PasswordEncoder;
import taskweaver.taskweaver_backend.api.member.controller.request.SignUpRequest;
import taskweaver.taskweaver_backend.api.member.service.response.CreateAccessTokenResponse;
import taskweaver.taskweaver_backend.api.member.service.response.SignInResponse;
import taskweaver.taskweaver_backend.api.member.service.response.SignUpResponse;
import taskweaver.taskweaver_backend.domain.member.model.LoginType;
import taskweaver.taskweaver_backend.domain.member.model.Member;

public class MemberConverter {

    public MemberConverter() {
    }

    public static Member toMember(SignUpRequest signUpRequest, PasswordEncoder encoder) {


        return Member.builder()
                .email(signUpRequest.email())
                .password(encoder.encode(signUpRequest.password()))
                .nickname(signUpRequest.nickname())
                .loginType(LoginType.findLoginType(signUpRequest.loginType()))
                .build();
    }

    public static SignUpResponse toSignUpResponse(Member member) {
        return new SignUpResponse(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                String.valueOf(member.getLoginType())
        );
    }

    public static SignInResponse toSignInResponse(Member member, String accessToken, String refreshToken) {
        return new SignInResponse(
                member.getId(),
                member.getEmail(),
                member.getLoginType(),
                member.getNickname(),
                accessToken,
                refreshToken
        );
    }


    public static CreateAccessTokenResponse toCreateAccessTokenResponse(String newAccessToken) {
        return new CreateAccessTokenResponse(newAccessToken);
    }
}
