package fit.fitspring.controller.dto.trainer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Schema(title = "리뷰정보 - 트레이너용")
public class ReviewDto {
    @Schema(description = "이름", example = "홍길동")
    private String name;
    @Schema(description = "프로필이미지", example = "1.jpg")
    private String profile;
    @Schema(description = "평점", example = "4.3")
    private int grade;
    @Schema(description = "작성일", example = "2023.01.10")
    private LocalDate createdAt;
    @Schema(description = "내용", example = "좋아요")
    private String contents;
    @Schema(description = "고객 id", example = "1")
    private Long customerIdx;

    public ReviewDto(String name, String profile, int grade, LocalDate createdAt, String contents, Long customerIdx) {
        this.name = name;
        this.profile = profile;
        this.grade = grade;
        this.createdAt = createdAt;
        this.contents = contents;
        this.customerIdx = customerIdx;
    }
}