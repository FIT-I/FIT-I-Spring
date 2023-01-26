package fit.fitspring.controller.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class WishDto {
    @Schema(description = "트레이너 유저식별자", example = "1")
    private Long trainerIdx;
    @Schema(description = "트레이너 이름", example = "이름")
    private String trainerName;
    @Schema(description = "트레이너 프로필이미지", example = "1.jpg(기본 프로필은 'trainerProfile'로 설정)")
    private String trainerProfile;
    @Schema(description = "트레이너 평점", example = "4.3")
    private float trainerGrade;
    @Schema(description = "트레이너 학교", example = "숭실대학교")
    private String trainerSchool;
    @Schema(description = "찜 날짜", example = "2023.1.17")
    private LocalDate createdAt;

    public WishDto(Long trainerIdx, String trainerName, String trainerProfile,
                   float trainerGrade, String trainerSchool, LocalDate createdAt) {
        this.trainerIdx = trainerIdx;
        this.trainerName = trainerName;
        this.trainerProfile = trainerProfile;
        this.trainerGrade = trainerGrade;
        this.trainerSchool = trainerSchool;
        this.createdAt = createdAt;
    }
}
