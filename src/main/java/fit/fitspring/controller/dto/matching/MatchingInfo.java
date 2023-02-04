package fit.fitspring.controller.dto.matching;

import fit.fitspring.domain.matching.MatchingOrder;
import fit.fitspring.domain.matching.PickUpType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Schema(title = "매칭 상세 정보")
public class MatchingInfo {
    @Schema(description = "매칭Id", example = "23")
    private Long matchingId;
    @Schema(description = "고객Id", example = "234")
    private Long customerId;
    @Schema(description = "고객이름", example = "홍길동")
    private String name;
    @Schema(description = "시간 당 가격", example = "20000")
    private String pricePerHour;
    @Schema(description = "총 금액", example = "20000")
    private String totalPrice;
    @Schema(description = "매칭 시작 날짜", example = "2023.01.25")
    private String matchingStart;
    @Schema(description = "매칭 끝나는 날짜", example = "2023.01.30")
    private String matchingFinish;
    @Schema(description = "매칭 기간", example = "6")
    private int matchingPeriod;
    @Schema(description = "픽업 타입", example = "TRAINER_GO")
    private PickUpType pickUpType;
    @Schema(description = "매칭 장소(픽업타입이 TRAINER_GO일 경우만 제공 됨)", example = "서울특별시 상도로 50")
    private String location;

    public MatchingInfo(MatchingOrder entity,String matchingStart, String MatchingFinish, int matchingPeriod) {
        this.matchingId = entity.getId();
        this.customerId = entity.getCustomer().getId();
        this.name = entity.getCustomer().getName();
        this.pricePerHour = String.valueOf(entity.getTrainer().getPriceHour());
        this.totalPrice = String.valueOf(entity.getTrainer().getPriceHour());
        this.matchingStart = matchingStart;
        this.matchingFinish = MatchingFinish;
        this.matchingPeriod = matchingPeriod;
        this.pickUpType = entity.getPickUpType();
        if(pickUpType==PickUpType.TRAINER_GO)
            this.location = entity.getCustomer().getLocation();
        else
            this.location=null;
    }
}
