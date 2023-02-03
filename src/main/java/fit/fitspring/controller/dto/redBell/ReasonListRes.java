package fit.fitspring.controller.dto.redBell;

import fit.fitspring.domain.redBell.ReasonForRedBell;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@Schema(title = "신고 사유")
@NoArgsConstructor
@AllArgsConstructor
public class ReasonListRes {
    @Schema(description = "name", example = "AD")
    private String name;
    @Schema(description = "description", example = "요청서와 관련없는 광고/홍보/영업")
    private String description;

    public ReasonListRes(ReasonForRedBell reason){
        this.name = reason.name();
        this.description = reason.getKrName();
    }
}
