package fit.fitspring.domain.matching;

import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.trainer.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchingOrderRepository extends JpaRepository<MatchingOrder, Long> {
    List<MatchingOrder> findAllByCustomerAndIsCompleteLikeOrderByIdDesc(@Param("customer")Account customer,@Param("isComplete") String isComplete);
    List<MatchingOrder> findAllByTrainerAndIsCompleteLikeOrderByIdDesc(@Param("trainer")Trainer trainer,@Param("isComplete")String isComplete);
    Boolean existsByCustomerAndTrainer(@Param("customer")Account customer, @Param("trainer")Trainer trainer);
}