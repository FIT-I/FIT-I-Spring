package fit.fitspring.controller.dto.matching;

import fit.fitspring.domain.matching.MatchingOrder;
import fit.fitspring.domain.matching.PickUpType;
import fit.fitspring.domain.trainer.Trainer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(title = "트레이너의 매칭 리스트")
public class MatchingListForTrainer {
    @Schema(description = "매칭Id", example = "23")
    private Long matchingId;
    @Schema(description = "고객Id", example = "234")
    private Long customerId;
    @Schema(description = "이름", example = "홍길동")
    private String name;
    @Schema(description = "프로필이미지", example = "customerProfile1")
    private String profile;
    @Schema(description = "픽업 타입", example = "TRAINER_GO")
    private PickUpType pickUpType;
    @Schema(description = "보낸 날짜", example = "2023.01.25")
    private String orderDate;
    @Schema(description = "보낸 날짜(몇일 전)", example = "2")
    private int orderDateGap;

    public MatchingListForTrainer(MatchingOrder entity, String orderDate, int orderDateGap) {
        this.matchingId = entity.getId();
        this.customerId = entity.getCustomer().getId();
        this.name = entity.getCustomer().getName();
        this.profile = entity.getCustomer().getProfile();
        this.pickUpType = entity.getPickUpType();
        this.orderDate = orderDate;
        this.orderDateGap = orderDateGap;
    }
}
