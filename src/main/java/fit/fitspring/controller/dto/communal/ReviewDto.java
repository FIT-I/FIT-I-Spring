package fit.fitspring.controller.dto.communal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(title = "리뷰정보")
public class ReviewDto {
    @Schema(description = "이름", example = "홍길동")
    private String name;
    @Schema(description = "평점", example = "4.3")
    private Float grade;
    @Schema(description = "작성일", example = "2023.01.10")
    private String createdAt;
    @Schema(description = "내용", example = "좋아요")
    private String contents;

    public ReviewDto(String name, Float grade, String createdAt, String contents) {
        this.name = name;
        this.grade = grade;
        this.createdAt = createdAt;
        this.contents = contents;
    }
}