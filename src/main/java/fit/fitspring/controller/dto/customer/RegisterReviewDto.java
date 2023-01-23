package fit.fitspring.controller.dto.customer;


import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.review.Review;
import fit.fitspring.domain.trainer.Trainer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RegisterReviewDto {
    @Schema(description = "트레이너 유저식별자", example = "1")
    private Long trainerIdx;
    @Schema(description = "별점", example = "4")
    private int grade;
    @Schema(description = "내용", example = "좋아요")
    private String contents;

    public Review toEntity(Account customer, Trainer trainer){
        return Review.builder()
                .customer(customer)
                .trainer(trainer)
                .grade(grade)
                .content(contents)
                .build();
    }
}
