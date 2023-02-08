package fit.fitspring.domain.redBell;

import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.trainer.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RedBellRepository extends JpaRepository<RedBell, Long> {
    Optional<RedBell> findFirstByCustomerAndTrainerOrderByCreatedDateDesc(@Param("customer") Account customer,@Param("trainer") Trainer trainer);
    Long countByTrainer(Trainer trainer);
}
