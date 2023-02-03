package fit.fitspring.controller.dto.matching;

import fit.fitspring.domain.matching.MatchingOrder;
import fit.fitspring.domain.trainer.Trainer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(title = "고객의 매칭 리스트")
public class MatchingListForCust {
    @Schema(description = "매칭Id", example = "23")
    private Long matchingId;
    @Schema(description = "트레이너Id", example = "234")
    private Long trainerId;
    @Schema(description = "이름", example = "홍길동")
    private String name;
    @Schema(description = "프로필이미지", example = "1.jpg")
    private String profile;
    @Schema(description = "학교", example = "숭실대학교 재학")
    private String school;
    @Schema(description = "평점", example = "4.8")
    private Float grade;
    @Schema(description = "보낸 날짜", example = "2023.01.25")
    private String orderDate;
    @Schema(description = "보낸 날짜(몇일 전인지)", example = "2")
    private int orderDateGap;

    @Schema(description = "오픈 채팅 링크 (수락 후 생성됨)", example = "bit.ly.,eqe")
    private String openChatLink;

    public MatchingListForCust(MatchingOrder entity, String orderDate, int orderDateGap) {
        this.matchingId = entity.getId();
        this.trainerId = entity.getTrainer().getId();
        this.name = entity.getTrainer().getUser().getName();
        this.profile = entity.getTrainer().getUserImg().getProfile();
        this.school = entity.getTrainer().getSchool();
        this.grade = entity.getTrainer().getGrade();
        this.openChatLink = entity.getOpenChatLink();
        this.orderDate = orderDate;
        this.orderDateGap = orderDateGap;
    }
}
