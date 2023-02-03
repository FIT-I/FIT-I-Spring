package fit.fitspring.controller.dto.redBell;

import fit.fitspring.domain.redBell.ReasonForRedBell;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@Schema(title = "신고 하기")
@NoArgsConstructor
@AllArgsConstructor
public class RedBellReq {
    @Schema(description = "트레이너Id", example = "101")
    private Long trainerId;
    @Schema(description = "신고 사유", example = "AD")
    private ReasonForRedBell reason;
}