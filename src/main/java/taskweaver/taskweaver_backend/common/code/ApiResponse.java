package taskweaver.taskweaver_backend.common.code;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiResponse<T> {

    // API 응답 결과 Response
    private T result;

    // API 응답 코드 Response
    private int resultCode;

    // API 응답 코드 Message
    private String resultMsg;

    @Builder
    public ApiResponse(final T result, final int resultCode, final String resultMsg) {
        this.result = result;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }


    /**
     * 데이터를 포함하는 성공 응답 생성 (SuccessCode 사용)
     * @param code 성공 코드 (e.g., SuccessCode.SELECT_SUCCESS)
     * @param data 반환할 데이터
     */
    public static <T> ApiResponse<T> onSuccess(SuccessCode code, T data) {
        return ApiResponse.<T>builder()
                .resultCode(code.getStatus())
                .resultMsg(code.getMessage())
                .result(data)
                .build();
    }

    /**
     * 데이터 없이 메시지만 있는 성공 응답 생성 (SuccessCode 사용)
     * @param code 성공 코드 (e.g., SuccessCode.DELETE_SUCCESS)
     */
    public static ApiResponse<?> onSuccess(SuccessCode code) {
        return ApiResponse.builder()
                .resultCode(code.getStatus())
                .resultMsg(code.getMessage())
                .build();
    }

    /**
     * 커스텀 메시지를 포함하는 성공 응답 생성 (SuccessCode 사용)
     * @param code 성공 코드 (e.g., SuccessCode.UPDATE_SUCCESS)
     * @param message 직접 작성한 성공 메시지
     */
    public static ApiResponse<?> onSuccess(SuccessCode code, String message) {
        return ApiResponse.builder()
                .resultCode(code.getStatus())
                .resultMsg(message)
                .build();
    }
}
