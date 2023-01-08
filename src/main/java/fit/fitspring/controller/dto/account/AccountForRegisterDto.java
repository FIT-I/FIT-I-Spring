package fit.fitspring.controller.dto.account;

import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.account.AccountType;

public class AccountForRegisterDto {
    private String email;
    private String password;
    private AccountType accountType;

    public AccountForRegisterDto(String email, String password, AccountType accountType) {
        this.email = email;
        this.password = password;
        this.accountType = accountType;
    }

    public Account toEntity(){
        return Account.builder()
                .email(email)
                .password(password)
                .accountType(accountType)
                .build();
    }
}
