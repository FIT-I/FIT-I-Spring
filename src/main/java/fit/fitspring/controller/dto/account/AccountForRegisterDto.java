package fit.fitspring.controller.dto.account;

import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.account.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountForRegisterDto {
    @Schema(description = "이름", example = "홍길동")
    private String name;
    @Schema(description = "이메일", example = "fiti@soongsil.ac.kr")
    private String email;
    @Schema(description = "비밀번호", example = "fiti123!")
    private String password;
    @Schema(description = "역할", example = "고객")
    private AccountType accountType;

    public AccountForRegisterDto(String name, String email, String password, AccountType accountType) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.accountType = accountType;
    }

    public Account toEntity(){
        return Account.builder()
                .name(name)
                .email(email)
                .password(password)
                .accountType(accountType)
                .build();
    }
}
