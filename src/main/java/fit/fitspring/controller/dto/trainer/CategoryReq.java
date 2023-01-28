package fit.fitspring.controller.dto.trainer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@Schema(title = "트레이너 카테고리수정")
@NoArgsConstructor
@AllArgsConstructor
public class CategoryReq {
    @Schema(description = "수정할 카테고리(pt/diet/food/rehab)", example = "food")
    private String Category;
}
