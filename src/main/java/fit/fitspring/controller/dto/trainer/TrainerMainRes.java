package fit.fitspring.controller.dto.trainer;

import fit.fitspring.domain.trainer.Trainer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@Schema(title = "트레이너 메인화면")
@NoArgsConstructor
@AllArgsConstructor
public class TrainerMainRes {
    @Schema(description = "트레이너Id", example = "1010")
    private Long id;
    @Schema(description = "이름", example = "홍길동")
    private String name;
    @Schema(description = "이메일", example = "abc@soongsil.ac.kr")
    private String email;
    @Schema(description = "카테고리", example = "개인 PT")
    private String category;
    @Schema(description = "등급명", example = "gold")
    private String levelName;
    @Schema(description = "평점", example = "4.3")
    private Float grade;
    @Schema(description = "자격증수", example = "2")
    private Long certificateNum;
    @Schema(description = "학교", example = "숭실대학교 재학")
    private String school;
    @Schema(description = "소개글", example = "안녕하세요")
    private String contents;

    public TrainerMainRes(Trainer entity,String category, Long certificateNum) {
        this.id = entity.getId();
        this.name = entity.getUser().getName();
        this.email = entity.getUser().getEmail();
        this.category = category;
        this.levelName = entity.getLevel().getName();
        this.school = entity.getSchool();
        this.grade = entity.getGrade();
        this.certificateNum = certificateNum;
        this.contents = entity.getIntro();
    }
}
