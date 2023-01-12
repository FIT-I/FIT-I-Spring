package fit.fitspring.controller.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(title = "트레이너검색분류")
public class SearchTrainerDto {
    @Schema(description = "PT분류", example = "식단관리")
    private String category;
    @Schema(description = "정렬순", example = "실시간순")
    private String listSort;

    public SearchTrainerDto(String category, String listSort) {
        this.category = category;
        this.listSort = listSort;
    }
}
