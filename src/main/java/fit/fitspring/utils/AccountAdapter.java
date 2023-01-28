package fit.fitspring.utils;

import fit.fitspring.controller.dto.account.AccountSessionDto;
import fit.fitspring.domain.account.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class AccountAdapter extends User {
    private final AccountSessionDto userDto;

    public AccountAdapter(Account account, List<GrantedAuthority> roles) {
        super(account.getEmail(), account.getPassword(), roles);

        this.userDto = AccountSessionDto.from(account);
    }

    public AccountSessionDto getUser() {
        return this.userDto;
    }
}

