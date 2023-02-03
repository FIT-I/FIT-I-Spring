package fit.fitspring.controller.dto.trainer;

import fit.fitspring.controller.dto.communal.ReviewDto;
import fit.fitspring.domain.trainer.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Schema(title = "트레이너정보")
public class TrainerInformationDto {
    @Schema(description = "이름", example = "홍길동")
    private String name;
    @Schema(description = "프로필이미지", example = "1.jpg")
    private String profile;
    @Schema(description = "배경이미지", example = "1.jpg")
    private String background;
    @Schema(description = "등급명", example = "gold")
    private String levelName;
    @Schema(description = "학교", example = "숭실대학교 재학")
    private String school;
    @Schema(description = "평점", example = "4.8")
    private Float grade;
    @Schema(description = "관리비용", example = "10000")
    private String cost;
    @Schema(description = "소개글", example = "안녕하세요")
    private String intro;
    @Schema(description = "서비스상세설명", example = "안녕하세요")
    private String service;
    @Schema(description = "리뷰리스트", example = "{홍길동, profile1, 4, 2023.1.20, 좋아요")
    private List<ReviewDto> reviewDto;
    @Schema(description = "사진및자격증리스트", example = "1.jpg")
    private List<String> imageList;
    @Schema(description = "매칭 관리(on/off)", example = "true")
    private boolean matching_state;
    @Schema(description = "카테고리", example = "개인 PT")
    private String category;
}