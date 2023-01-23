package fit.fitspring.controller.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "로그인정보")
public class LoginReqDto {

    @Schema(description = "이메일", example = "fiti@soongsil.ac.kr")
    private String email;
    @Schema(description = "비밀번호", example = "fiti123!")
    private String password;


}
