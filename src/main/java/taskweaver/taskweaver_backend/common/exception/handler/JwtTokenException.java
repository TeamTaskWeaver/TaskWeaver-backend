package taskweaver.taskweaver_backend.common.exception.handler;


import lombok.Builder;
import taskweaver.taskweaver_backend.common.code.ErrorCode;

public class JwtTokenException extends RuntimeException {

    private final ErrorCode errorCode;

    @Builder
    public JwtTokenException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    @Builder
    public JwtTokenException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
