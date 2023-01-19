package fit.fitspring.exception.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    /*공통*/
    INVALID_INPUT(HttpStatus.BAD_REQUEST.value(), "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED.value(), "잘못된 호출입니다."),
    HANDLE_ACCESS_DENIED(HttpStatus.UNAUTHORIZED.value(), "접근할 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "문제가 발생했습니다."),

    /*유저*/
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다."),
    DUPLICATE_ACCOUNT(HttpStatus.CONFLICT.value(), "이미 사용중인 이메일입니다."),

    ;

    private int status;
    private final String message;

    ErrorCode(final int status, final String message) {
        this.status = status;
        this.message = message;
    }
}
