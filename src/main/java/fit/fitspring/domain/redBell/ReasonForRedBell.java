package fit.fitspring.domain.redBell;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Locale;

public enum ReasonForRedBell {
    AD("요청서와 관련없는 광고/홍보/영업"),
    DUPLICATE_REQUEST("중복 요청서 작성"),
    FALSE_REQUEST("허위 요청서 작성"),
    PERSONAL_INFORMATION_EXPOSURE("요청서 내 개인정보 노출"),
    INCORRECT_CATEGORY("서비스 카테고리 오선택"),
    INVALID_PHONE_NUM("유효하지 않은 전화번호"),
    INAPPROPRIATE_WORD("부적절한 언어(욕설, 성희롱 등) 사용"),
    ILLEGAL("관련 법령 등을 위반하는 요청");

    private String krName;

    @JsonCreator
    public static ReasonForRedBell from(String s){
        return ReasonForRedBell.valueOf(s.toUpperCase(Locale.ROOT));
    }

    ReasonForRedBell(String krName) {
        this.krName = krName;
    }

    public String getKrName() {
        return this.krName;
    }
}
