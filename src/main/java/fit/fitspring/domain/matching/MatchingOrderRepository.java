package fit.fitspring.domain.matching;

import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.trainer.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchingOrderRepository extends JpaRepository<MatchingOrder, Long> {

    List<MatchingOrder> findAllByCustomerAndIsCompleteLikeOrderByIdDesc(Account customer,String isComplete);
    List<MatchingOrder> findAllByTrainerAndIsCompleteLikeOrderByIdDesc(Trainer trainer,String isComplete);
}