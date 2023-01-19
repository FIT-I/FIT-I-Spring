package fit.fitspring.domain.trainer;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface TrainerRepositoryCustom {
    /**
     * 해당 카테고리의 트레이너 리스트 실시간 순으로 페이징 처리해서 받아줌
     */
    Slice<Trainer> findByCategoryOrderByUploadDateDesc(Category category, Long lastTrainerId, Pageable pageable);

    /**
     * 해당 카테고리의 트레이너 리스트 레벨 순으로 페이징 처리해서 받아줌
     */
    Slice<Trainer> findByCategoryOrderByLevelDesc(Category category, Long lastTrainerId, Pageable pageable);

    /**
     * 해당 카테고리의 트레이너 리스트 낮은 가격 순으로 페이징 처리해서 받아줌
     */
    Slice<Trainer> findByCategoryOrderByCostDesc(Category category, Long lastTrainerId, Pageable pageable);

}
