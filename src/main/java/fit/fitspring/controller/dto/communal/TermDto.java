package fit.fitspring.controller.dto.communal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(title = "이용약관정보")
public class TermDto {
    @Schema(description = "이용약관 식별자", example = "1")
    private Long termIdx;
    @Schema(description = "약관명", example = "개인정보 정보동의")
    private String termName;
    @Schema(description = "내용", example = "제1항에의거")
    private String termDetail;

    public TermDto(Long termIdx, String termName, String termDetail) {
        this.termIdx = termIdx;
        this.termName = termName;
        this.termDetail = termDetail;
    }
}
