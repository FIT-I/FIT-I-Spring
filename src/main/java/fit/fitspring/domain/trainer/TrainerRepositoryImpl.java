package fit.fitspring.domain.trainer;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TrainerRepositoryImpl implements TrainerRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    private final QTrainer trainer = QTrainer.trainer;
    private final QLevel level = QLevel.level;

    @Override
    public Slice<Trainer> findByCategoryOrderByIdeDesc(Category category, Long lastTrainerId, Pageable pageable) {
        List<Trainer> trainerList = queryFactory.selectFrom(trainer)
                .where(
                        lastTrainerId(lastTrainerId),

                        trainer.category.eq(category)
                )
                .orderBy(trainer.id.desc())
                .limit(pageable.getPageSize()+1)
                .fetch();

        return checkLastPage(pageable, trainerList);

    }

    @Override
    public Slice<Trainer> findByCategoryOrderByLevelDesc(Category category, Long lastTrainerId,Long lastLevelId, Pageable pageable) {
        List<Trainer> trainerList = queryFactory.selectFrom(trainer)
                .where(
                    cursorLevelAndTrainerId(lastTrainerId, lastLevelId),
                        trainer.category.eq(category)
                )
                .orderBy(level.id.asc(), trainer.id.desc())
                .limit(pageable.getPageSize()+1)
                .fetch();
        return checkLastPage(pageable, trainerList);
    }

    @Override
    public Slice<Trainer> findByCategoryOrderByPriceDesc(Category category, Long lastTrainerId, Integer lastPrice, Pageable pageable) {
        List<Trainer> trainerList = queryFactory.selectFrom(trainer)
                .where(
                        cursorCostAndTrainerId(lastTrainerId, lastPrice, Sort.Direction.DESC),
                        trainer.category.eq(category)
                )
                .orderBy(trainer.priceHour.desc(),trainer.id.desc())
                .limit(pageable.getPageSize()+1)
                .fetch();

        return checkLastPage(pageable, trainerList);
    }
    @Override
    public Slice<Trainer> findByCategoryOrderByPriceAsc(Category category, Long lastTrainerId, Integer lastPrice, Pageable pageable) {
        List<Trainer> trainerList = queryFactory.selectFrom(trainer)
                .where(
                        cursorCostAndTrainerId(lastTrainerId, lastPrice, Sort.Direction.ASC),
                        trainer.category.eq(category)
                )
                .orderBy(trainer.priceHour.asc(),trainer.id.desc())
                .limit(pageable.getPageSize()+1)
                .fetch();

        return checkLastPage(pageable, trainerList);
    }

    private BooleanExpression lastTrainerId(Long trainerId) {
        if (trainerId == null) {
            return null;
        }
        return trainer.id.lt(trainerId); //trainerId < lastTrainerId
    }

    private BooleanExpression cursorLevelAndTrainerId(Long trainerId, Long levelId) {
        if (trainerId == null) {
            return null;
        }
        //levelId 골드->1 브론즈->3로 가정
        return trainer.level.id.eq(levelId)
                .and(trainer.id.lt(trainerId)) //(lastLevelId == levelId) && (trainerId < lastTrainerId)
                .or(trainer.level.id.gt(levelId)); // lastLevelId<levelId
    }

    private BooleanExpression cursorCostAndTrainerId(Long trainerId, Integer price, Sort.Direction direction) {
        if (trainerId == null) {
            return null;
        }

        if(direction.isDescending()) {
            return trainer.priceHour.eq(price)
                    .and(trainer.id.lt(trainerId))
                    .or(trainer.priceHour.lt(price));
        }else{
            return trainer.priceHour.eq(price)
                    .and(trainer.id.lt(trainerId))
                    .or(trainer.priceHour.gt(price));
        }
    }

    // 무한 스크롤 방식 처리하는 메서드
    private Slice<Trainer> checkLastPage(Pageable pageable, List<Trainer> results) {

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

}
