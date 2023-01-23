package fit.fitspring.controller.dto.account;

import lombok.Getter;
import lombok.Setter;

@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성

public class RegisterResDto {
    private String success;

    public RegisterResDto(String name) {
        this.success = name + " 님, 환영합니다.";
    }
}
