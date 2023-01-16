package fit.fitspring.service;

import fit.fitspring.controller.dto.account.AccountForRegisterDto;
import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.account.AccountRepository;
import fit.fitspring.exception.account.DuplicatedAccountException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    public void registerAccount(AccountForRegisterDto accountDto) {
        Account account = accountDto.toEntity();
        try {
            accountRepository.save(account);
        } catch (DataIntegrityViolationException e){
            throw new DuplicatedAccountException();
        }
    }
}
