package fit.fitspring.domain.account;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository {
    Optional<Account> findByEmail(String email);
}
