package fit.fitspring.domain.matching;

import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.trainer.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    Boolean existsByCustomerAndTrainer(Account customer, Trainer trainer);

    Optional<WishList> findByCustomerAndTrainer(Account customer, Trainer trainer);
}
