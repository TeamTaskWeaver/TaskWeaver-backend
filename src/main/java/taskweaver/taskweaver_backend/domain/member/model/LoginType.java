package taskweaver.taskweaver_backend.domain.member.model;

import taskweaver.taskweaver_backend.common.code.ErrorCode;
import taskweaver.taskweaver_backend.common.exception.handler.BusinessExceptionHandler;

import java.util.Arrays;

public enum LoginType {
    DEFAULT, KAKAO, NAVER, APPLE;
    public static LoginType findLoginType(String loginType) {
        return Arrays.stream(LoginType.values())
                .filter(stateName -> stateName.toString().equals(loginType))
                .findFirst()
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.LOGIN_TYPE_NOT_FOUND));
    }
}
