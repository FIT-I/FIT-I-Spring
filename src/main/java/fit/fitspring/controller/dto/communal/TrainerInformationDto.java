package fit.fitspring.controller.dto.communal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
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
    @Schema(description = "거리", example = "1km")
    private String distance;
    @Schema(description = "평점", example = "4.8")
    private Float grade;
    @Schema(description = "자격증리스트", example = "생활체육지도사 1급")
    private List<String> certificateList;
    @Schema(description = "관리비용리스트", example = "10000, 5000")
    private List<Integer> costList;
    @Schema(description = "소개글", example = "안녕하세요")
    private String contents1;
    @Schema(description = "서비스상세설명", example = "안녕하세요")
    private String contents2;

    private ReviewDto reviewDto;
    @Schema(description = "평균평점", example = "4.5")
    private Float average;

    @Schema(description = "사진및자격증리스트", example = "1.jpg")
    private List<String> imageList;

    public TrainerInformationDto(){

    }
}