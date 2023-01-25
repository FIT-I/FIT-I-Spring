package fit.fitspring.domain.trainer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerCertRepository extends JpaRepository<TrainerCert, Long> {
    Long countByTrainer(Trainer trainer);
}
