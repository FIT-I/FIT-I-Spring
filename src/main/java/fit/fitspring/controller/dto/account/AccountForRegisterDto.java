package fit.fitspring.controller.dto.account;

import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.account.AccountType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountForRegisterDto {
    private String name;
    private String email;
    private String password;
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
