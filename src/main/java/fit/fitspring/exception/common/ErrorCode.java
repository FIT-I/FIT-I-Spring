package fit.fitspring.exception.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    /*성공 1000*/
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /*공통 2000*/
    INVALID_INPUT(false, 2000, "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED(false, 2001, "잘못된 호출입니다."),
    HANDLE_ACCESS_DENIED(false, 2002, "접근할 수 없습니다."),
    INTERNAL_SERVER_ERROR(false, 2003, "문제가 발생했습니다."),
    EMPTY_JWT(false, 2004, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2005, "유효하지 않은 JWT입니다."),

    /*유저 3000*/
    ACCOUNT_NOT_FOUND(false, 3001, "사용자를 찾을 수 없습니다."),
    DUPLICATE_ACCOUNT(false, 3002, "이미 사용중인 이메일입니다."),
    EMAIL_SENDING_ERROR(false, 3003, "이메일 전송에 실패하였습니다."),
    POST_ACCOUNTS_EMPTY_EMAIL(false, 3004, "이메일을 입력해주세요."),
    POST_ACCOUNTS_EMPTY_NAME(false, 3005, "이름을 입력해주세요."),
    POST_ACCOUNTS_INVALID_EMAIL(false, 3006, "이메일 형식이 잘 못 되었습니다."),
    FAILED_TO_LOGIN(false, 3007, "비밀번호가 틀렸습니다."),
    IS_NOT_TRAINER(false,3008, "트레이너가 아닙니다."),
    INVALID_USERIDX(false, 3009, "잘못된 유저 식별자입니다."),
    INVALID_TRAINERIDX(false, 3010, "잘못된 트레이너 식별자입니다."),

    /*서버, DB 4000*/
    DATABASE_ERROR(false, 4000, "DB에 문제가 발생했습니다."),
    DB_INSERT_ERROR(false, 4001, "데이터 저장 오류가 발생하였습니다."),
    ;

    private final boolean isSuccess;
    private final int code;
    private final String message;

    ErrorCode(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

}
