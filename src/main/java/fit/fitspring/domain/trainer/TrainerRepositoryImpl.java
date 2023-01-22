package fit.fitspring.domain.trainer;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TrainerRepositoryImpl implements TrainerRepositoryCustom{

    //private final JPAQueryFactory queryFactory;
    /*private final QTrainer trainer = QTrainer.trainer;*/

    @Override
    public Slice<Trainer> findByCategoryOrderByUploadDateDesc(Category category, Long lastTrainerId, Pageable pageable) {
        return null;

    }

    @Override
    public Slice<Trainer> findByCategoryOrderByLevelDesc(Category category, Long lastTrainerId, Pageable pageable) {
        return null;
    }

    @Override
    public Slice<Trainer> findByCategoryOrderByCostDesc(Category category, Long lastTrainerId, Pageable pageable) {
        return null;
    }
}
