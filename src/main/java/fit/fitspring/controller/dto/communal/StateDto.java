package fit.fitspring.controller.dto.communal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "유저의 state 조회")
public class StateDto {
    @Schema(description = "유저 상태(A:활성상태, D:비활성화 상태, W:탈퇴한 상태)", example = "A")
    private String state;
}
