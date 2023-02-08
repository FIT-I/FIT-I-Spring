package fit.fitspring.domain.redBell;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Locale;

public enum ReasonForRedBell {
    AD("요청서와 관련없는 광고/홍보/영업"),
    FAKE_PROFILE("허위 프로필 작성"),
    INVALID_LINK("유효하지 않은 카카오톡 링크"),
    BAD_WORD("부적절한 언어(욕설, 성희롱 등) 사용"),
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
