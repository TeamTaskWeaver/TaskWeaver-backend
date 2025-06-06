package taskweaver.taskweaver_backend.common.exception.handler;


import lombok.Builder;
import lombok.Getter;
import taskweaver.taskweaver_backend.common.code.ErrorCode;

@Getter
public class BusinessExceptionHandler extends RuntimeException {

    private final ErrorCode errorCode;

    @Builder
    public BusinessExceptionHandler(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    @Builder
    public BusinessExceptionHandler(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
