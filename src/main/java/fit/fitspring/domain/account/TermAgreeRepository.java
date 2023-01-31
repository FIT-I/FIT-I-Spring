package fit.fitspring.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermAgreeRepository extends JpaRepository<TermAgree, Long> {
    List<TermAgree> findAllByUser(Account user);
}