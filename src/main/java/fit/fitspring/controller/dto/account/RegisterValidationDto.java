package fit.fitspring.controller.dto.account;

import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.account.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@Schema(title = "회원정보")
public class RegisterValidationDto {
    @Schema(description = "이름", example = "홍길동")
    private String name;
    @Schema(description = "이메일", example = "fiti@soongsil.ac.kr")
    private String email;
    @Schema(description = "비밀번호", example = "fiti123!")
    private String password;


    public RegisterValidationDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Account toEntity(){
        return Account.builder()
                .name(name)
                .email(email)
                .password(password)
                .profile("img_test")
                .accountType(AccountType.CUSTOMER)
                .build();
    }
}
