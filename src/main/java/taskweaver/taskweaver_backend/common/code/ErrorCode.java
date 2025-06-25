package taskweaver.taskweaver_backend.common.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    /**
     * ******************************* Global Error CodeList ***************************************
     * HTTP Status Code
     * 400 : Bad Request
     * 401 : Unauthorized
     * 403 : Forbidden
     * 404 : Not Found
     * 500 : Internal Server Error
     * *********************************************************************************************
     */
    // 잘못된 서버 요청
    BAD_REQUEST_ERROR(400, "G001", "Bad Request Exception"),

    // @RequestBody 데이터 미 존재
    REQUEST_BODY_MISSING_ERROR(400, "G002", "Required request body is missing"),

    // 유효하지 않은 타입
    INVALID_TYPE_VALUE(400, "G003", " Invalid Type Value"),

    // Request Parameter 로 데이터가 전달되지 않을 경우
    MISSING_REQUEST_PARAMETER_ERROR(400, "G004", "Missing Servlet RequestParameter Exception"),

    // 입력/출력 값이 유효하지 않음
    IO_ERROR(400, "G005", "I/O Exception"),

    // com.google.gson JSON 파싱 실패
    JSON_PARSE_ERROR(400, "G006", "JsonParseException"),

    // com.fasterxml.jackson.core Processing Error
    JACKSON_PROCESS_ERROR(400, "G007", "com.fasterxml.jackson.core Exception"),

    // 권한이 없음
    FORBIDDEN_ERROR(403, "G008", "Forbidden Exception"),

    // 서버로 요청한 리소스가 존재하지 않음
    NOT_FOUND_ERROR(404, "G009", "Not Found Exception"),

    // NULL Point Exception 발생
    NULL_POINT_ERROR(404, "G010", "Null Point Exception"),

    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_ERROR(400, "G011", "handle Validation Exception"),

    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_HEADER_ERROR(400, "G012", "Header에 데이터가 존재하지 않는 경우 "),

    // 서버가 처리 할 방법을 모르는 경우 발생
    INTERNAL_SERVER_ERROR(500, "G999", "Internal Server Error Exception"),

    // 토큰 만료 기한이 지났을 때 발생
    EXPIRED_JWT_ERROR(404, "G013", "The provided JWT token is expired"),

    // 토큰 유효성 검사가 실패할 때 발생
    INVALID_JWT_ERROR(404, "G014", "The provided JWT token is invalid"),

    // 토큰 검사시 사용자 인증 실패할 때 발생
    USER_AUTH_ERROR(404, "G015", "User authentication failed"),
    
    // 지원하지 않는 JWT 토큰일 때 발생
    UNSUPPORTED_JWT_TOKEN(400,"G017", "The provided JWT token is not supported"),
    EXPIRED_REFRESH_TOKEN(400, "A005", "만료된 Refresh Token입니다."),
    REFRESH_TOKEN_NOT_FOUND(400, "A006", "DB에 Refresh Token이 없습니다."),
    // 토큰이 없을 때 발생
    TOKEN_MISSING_ERROR(401, "G018", "Token is missing."),

    // 이미 로그아웃된 회원의 토큰일 때
    MEMBER_LOGGED_OUT(401, "G019", "User has already logged out."),
    FORBIDDEN_ACCESS(403, "C005", "접근 권한이 없습니다."),
    INVALID_REFRESH_TOKEN(400, "A004", "유효하지 않은 Refresh Token입니다."),
    /**
     * ******************************* Custom Error CodeList ***************************************
     */
    // Transaction Insert Error
    INSERT_ERROR(500, "9999", "Insert Transaction Error Exception"),

    // Transaction Update Error
    UPDATE_ERROR(500, "9999", "Update Transaction Error Exception"),

    // Transaction Delete Error
    DELETE_ERROR(500, "9999", "Delete Transaction Error Exception"),

    // PROJECT
    MEMBER_NOT_BELONG_TO_TEAM(400, "P001", "Member doesn't belong to this team"),
    PROJECT_NOT_FOUND(404, "P002", "Project Not Found"),
    NOT_PROJECT_MANAGER(403, "P003", "Only Project manager can do this work."),
    PROJECT_STATE_NOT_FOUND(404, "POO4", "Project State Not Found"),
    PROJECT_MEMBER_NOT_FOUND(404, "P005", "Project Member Not Found"),
    MANAGER_ID_NOT_IN_MEMBER_ID_LIST(400, "P006", "Member id list doesn't include manager id. Include manager id in member id list"),

    // TEAM
    TEAM_NOT_FOUND(404, "T001", "해당 팀을 찾을 수 없습니다."),
    TEAM_INVITE_LINK_NOT_FOUND(404, "T002", "유효하지 않은 초대 링크입니다."),
    ALREADY_TEAM_MEMBER(409, "T003", "이미 해당 팀의 멤버입니다."),
    CANNOT_APPOINT_SELF_AS_LEADER_AGAIN(400, "T004", "이미 팀장인 멤버에게 위임할 수 없습니다."),
    MEMBER_NOT_FOUND_IN_TEAM(404, "T005", "해당 멤버를 팀에서 찾을 수 없습니다."),
    EMPTY_MEMBER_LIST_TO_REMOVE(400, "T006", "내보낼 팀원 목록이 비어있습니다."),
    CANNOT_REMOVE_LEADER(403, "T007", "팀장은 내보낼 수 없습니다. 먼저 팀장을 위임해주세요."),
    NOT_TEAM_LEADER(403, "T008", "팀장 권한이 없습니다."),
    INVITATION_ALREADY_SENT(409, "T009", "이미 초대 요청을 보낸 멤버입니다."),
    INVALID_INVITE_RESPONSE(400, "T010", "초대 응답은 수락(1) 또는 거절(2)만 가능합니다."),
    TEAM_MEMBER_STATE_NOT_FOUND(404, "T011", "해당 초대 상태 정보를 찾을 수 없습니다."),
    CANNOT_INVITE_SELF(400, "T012", "자기 자신을 팀에 초대할 수 없습니다."),

    //TASK
    TASK_NOT_FOUND(404, "TS001", "Task Not Found"),
    TASK_STATE_NOT_FOUND(404, "TS002", "Task State Not Found"),
    TASK_MEMBER_NOT_FOUND(404, "TS003", "Task Member Not Found"),

    // MEMBER
    DUPLICATED_EMAIL(409, "M001", "이미 사용중인 이메일입니다."),
    MEMBER_NOT_FOUND(404, "M002", "해당 회원을 찾을 수 없습니다."),
    PASSWORD_NOT_MATCH(401, "M003", "비밀번호가 일치하지 않습니다."),
    SAME_PASSWORD(400, "M004", "새 비밀번호는 현재 비밀번호와 같을 수 없습니다."),
    CANNOT_WITHDRAW_TEAM_LEADER(403, "M005", "팀장 권한을 위임하기 전까지 탈퇴할 수 없습니다."),
    CANNOT_WITHDRAW_PROJECT_LEADER(403, "M006", "프로젝트 리더 권한을 위임하기 전까지 탈퇴할 수 없습니다."),
    PROFILE_IMAGE_UPLOAD_FAILED(500, "M007", "프로필 이미지 업로드에 실패했습니다."),
    LOGIN_TYPE_NOT_FOUND(404, "M008", "존재하지 않는 로그인 타입입니다."),
    DUPLICATED_NICKNAME(409, "M009", "이미 사용중인 닉네임입니다."),
    INVALID_PASSWORD_POLICY(400, "M010", "비밀번호는 정책에 맞지 않습니다."),


    // MEETING & AGENDA & RETROSPECTIVE (회의록 관련)
    MEETING_RECORD_NOT_FOUND(404, "MR001", "해당 회의록을 찾을 수 없습니다."),
    AGENDA_NOT_FOUND(404, "MR002", "해당 아젠다를 찾을 수 없습니다."),
    RETROSPECTIVE_NOT_FOUND(404, "MR003", "해당 회고록을 찾을 수 없습니다."),

    // COMMENT
    COMMENT_NOT_FOUND(404, "C001", "해당 댓글을 찾을 수 없습니다."),
    COMMENT_DEPTH_EXCEEDED(400, "C002", "댓글은 1단계 깊이까지만 작성할 수 있습니다."),
    NOT_COMMENT_WRITER(403, "C003", "댓글 작성자만 수정 및 삭제할 수 있습니다."),

    // EXTERNAL & ETC (외부 연동 및 기타)
    EMAIL_SEND_FAILED(500, "E001", "이메일 발송 중 오류가 발생했습니다."),
    KAKAO_TOKEN_PARSE_FAILED(500, "K001", "카카오 토큰 정보를 받아오는데 실패했습니다."),
    KAKAO_PROFILE_PARSE_FAILED(500, "K002", "카카오 프로필 정보를 받아오는데 실패했습니다.");

    /**
     * ******************************* Error Code Constructor ***************************************
     */
    // 에러 코드의 '코드 상태'을 반환한다.
    private final int status;

    // 에러 코드의 '코드간 구분 값'을 반환한다.
    private final String divisionCode;

    // 에러 코드의 '코드 메시지'을 반환한다.
    private final String message;

    // 생성자 구성
    ErrorCode(final int status, final String divisionCode, final String message) {
        this.status = status;
        this.divisionCode = divisionCode;
        this.message = message;
    }
}
