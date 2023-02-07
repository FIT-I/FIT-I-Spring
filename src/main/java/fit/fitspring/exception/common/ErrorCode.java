package fit.fitspring.exception.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    /*성공 1000*/
    SUCCESS(true, 1000, "요청에 성공하였습니다."),
    SAVE_AUTH(true, 1001, "인증 정보를 저장했습니다."),

    /*공통 2000*/
    INVALID_INPUT(false, 2000, "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED(false, 2001, "잘못된 호출입니다."),
    HANDLE_ACCESS_DENIED(false, 2002, "접근할 수 없습니다."),
    INTERNAL_SERVER_ERROR(false, 2003, "문제가 발생했습니다."),
    WRONG_JWT(false, 2004, "잘못된 JWT 서명입니다."),
    EXPIRED_JWT(false, 2005, "만료된 JWT 토큰입니다."),
    UNSUPPORTED_JWT(false, 2006, "지원되지 않는 JWT 토큰입니다."),
    ILLEGAL_JWT(false, 2007, "JWT 토큰이 잘못되었습니다."),
    INVALID_JWT(false, 2008, "유효하지 않은 JWT 토큰입니다."),
    ISSUE_JWT(false, 2009, "JWT 토큰 재발급에 실패했습니다."),


    /*유저 3000*/
    ACCOUNT_NOT_FOUND(false, 3001, "사용자를 찾을 수 없습니다."),
    DUPLICATE_ACCOUNT(false, 3002, "이미 사용중인 이메일입니다."),
    EMAIL_SENDING_ERROR(false, 3003, "이메일 전송에 실패하였습니다."),
    POST_ACCOUNTS_EMPTY_EMAIL(false, 3004, "이메일을 입력해주세요."),
    POST_ACCOUNTS_EMPTY_NAME(false, 3005, "이름을 입력해주세요."),
    POST_ACCOUNTS_INVALID_EMAIL(false, 3006, "이메일 형식이 잘못되었습니다."),
    FAILED_TO_LOGIN(false, 3007, "비밀번호가 틀렸습니다."),
    IS_NOT_TRAINER(false,3008, "트레이너가 아닙니다."),
    INVALID_USERIDX(false, 3009, "잘못된 유저 식별자입니다."),
    INVALID_TRAINERIDX(false, 3010, "잘못된 트레이너 식별자입니다."),
    INVALID_EMAIL(false, 3011, "존재하지 않는 이메일입니다."),
    DELETE_ACCOUNT(false, 3012, "탈퇴한 계정입니다."),
    DISAGREE_TERM(false, 3013, "이용 약관에 동의하지 않으셨습니다."),
    POST_ACCOUNTS_EMPTY_PWD(false, 3014, "비밀번호를 입력하지 않으셨습니다."),
    ALREADY_REGISTERED_REVIEW(false, 3015, "이미 리뷰를 작성한 트레이너입니다."),
    DENIED_REGISTER(false, 3016,"탈퇴한 계정으로 다시 가입할 수 없습니다."),
    POST_ACCOUNTS_EMPTY_MAJOR(false, 3017, "전공을 입력하지 않으셨습니다."),

    /*서버, DB 4000*/
    DATABASE_ERROR(false, 4000, "DB에 문제가 발생했습니다."),
    DB_INSERT_ERROR(false, 4001, "데이터 저장 오류가 발생하였습니다."),
    DB_MODIFY_ERROR(false, 4002, "데이터 수정 오류가 발생하였습니다."),
    NO_EXIST_LEVEL_DATA(false, 4003, "DB에 LEVEL 데이터가 존재하지 않습니다."),


    /*매칭, 좋아요 5000*/
    MATCHING_NOT_FOUND(false, 5000, "해당 매칭을 찾을 수 없습니다."),
    REJECTED_MATCHING(false, 5001, "거절된 매칭입니다."),
    INVALID_DATE_FORMAT(false, 5002, "매칭 날짜 형식이 잘못되었습니다."),
    PERMISSION_DENIED(false, 5003, "권한이 없습니다."),
    ALREADY_MATCHING(false, 5004, "이미 매칭신청했던 트레이너 입니다."),
    ALREADY_LIKE(false,5005, "이미 찜한 트레이너입니다."),

    /* 사진 6000*/
    NOT_FOUND_IMG(false, 6001, "해당 사진이 존재하지 않습니다."),


    /*AWS 7000*/
    AWS_S3UPLOADER_ERROR(false, 7001, "aws s3 이미지 업로드 오류가 발생하였습니다."),

    /*알림 토큰 8000*/
    SOMEONE_HAS_FCM_TOKEN(false, 8001, "동일한 토큰을 타인이 가지고 있습니다."),
    ALREADY_HAS_FCM_TOKEN(false, 8002, "동일한 토큰을 이미 가지고 있습니다.")


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
