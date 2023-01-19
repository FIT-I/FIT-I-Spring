package fit.fitspring.domain.trainer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository extends JpaRepository<Trainer, Long>, TrainerRepositoryCustom {

    boolean existsById(Long id);
}

