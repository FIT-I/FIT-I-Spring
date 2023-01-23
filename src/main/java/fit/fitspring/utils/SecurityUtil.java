package fit.fitspring.utils;

import fit.fitspring.domain.account.AccountRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public class SecurityUtil {

    private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);
    private static AccountRepository accountRepository;

    private SecurityUtil(AccountRepository accountRepository) { this.accountRepository = accountRepository; }



    // api 호출 시, 어떤 account에서 api를 요청했는지 조회하는 코드 : user_idx 반환
    public static Long getCurrentAccountId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String email;
        if (authentication == null || authentication.getName() == null){
            throw new RuntimeException("No authentication information.");
        }

        email = authentication.getName();

        return accountRepository.findByEmail(email).get().getId();
    }


    // api 호출 시, 어떤 account에서 api를 요청했는지 조회하는 코드 : user_email 반환
    public static String getCurrentAccountEmail() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null){
            throw new RuntimeException("No authentication information.");
        }
        return authentication.getName();
    }
}
