package fit.fitspring.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermAgreeRepository extends JpaRepository<TermAgree, Long> {
}