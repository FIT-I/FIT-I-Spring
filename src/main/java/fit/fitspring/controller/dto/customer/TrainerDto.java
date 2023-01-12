package fit.fitspring.controller.dto.customer;

import fit.fitspring.controller.dto.communal.ReviewDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@Schema(title = "트레이너정보")
public class TrainerDto {

    @Schema(description = "이름", example = "홍길동")
    private String name;
    @Schema(description = "프로필이미지", example = "1.jpg")
    private String profile;
    @Schema(description = "등급명", example = "gold")
    private String levelName;
    @Schema(description = "학교", example = "숭실대학교 재학")
    private String school;
    @Schema(description = "평점", example = "4.8")
    private Float grade;
    @Schema(description = "자격증수", example = "2")
    private Integer certificateNum;
    @Schema(description = "소개글", example = "안녕하세요")
    private String contents;
    @Schema(description = "비용", example = "10,000")
    private String cost;
}
