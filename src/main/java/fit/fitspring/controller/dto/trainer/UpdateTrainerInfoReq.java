package fit.fitspring.controller.dto.trainer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@Schema(title = "트레이너정보수정")
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTrainerInfoReq {
    @Schema(description = "이름", example = "홍길동")
    private String name;
    @Schema(description = "시간당 비용", example = "10000")
    private int costHour;
    @Schema(description = "소개글", example = "안녕하세요")
    private String intro;
    @Schema(description = "서비스상세설명", example = "안녕하세요")
    private String serviceDetail;

}
