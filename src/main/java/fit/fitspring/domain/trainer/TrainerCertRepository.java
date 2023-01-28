package fit.fitspring.domain.trainer;

import fit.fitspring.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerCertRepository extends JpaRepository<TrainerCert, Long> {
    Long countByTrainer(@Param("trainer")Trainer trainer);
}
