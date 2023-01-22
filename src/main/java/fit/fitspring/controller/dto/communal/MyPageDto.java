package fit.fitspring.controller.dto.communal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Schema(title = "마이페이지 간단정보")
public class MyPageDto {

    @Schema(description = "유저 식별자", example = "1")
    private Long userIdx;
    @Schema(description = "이름", example = "홍길동")
    private String userName;
    @Schema(description = "프로필 이미지", example = "1.jpg")
    private String profile;
    @Schema(description = "이메일", example = "fiti@soongsil.ac.kr")
    private String email;
    @Schema(description = "매칭 위치", example = "서울특별시")
    private String location;

}