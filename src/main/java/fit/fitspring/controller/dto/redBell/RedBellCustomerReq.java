package fit.fitspring.controller.dto.redBell;

import fit.fitspring.domain.redBell.ReasonForRedBell;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@Schema(title = "트레이너가 고객 신고하기")
@NoArgsConstructor
@AllArgsConstructor
public class RedBellCustomerReq {
    @Schema(description = "고객Id", example = "101")
    private Long customerId;
    @Schema(description = "신고 사유", example = "AD")
    private ReasonForRedBell reason;
}
