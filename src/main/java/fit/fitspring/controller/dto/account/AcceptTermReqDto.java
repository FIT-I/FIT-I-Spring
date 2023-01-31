package fit.fitspring.controller.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "이용 약관 동의 정보")
public class AcceptTermReqDto {
    @Schema(description = "개인정보 처리 방침 동의", example = "true")
    private Boolean term1;
    @Schema(description = "서비스 이용약관 동의 ", example = "true")
    private Boolean term2;
    @Schema(description = "전자금융거래 이용약관 동의", example = "true")
    private Boolean term3;

//    public AcceptTermReqDto(Boolean term1, Boolean term2, Boolean term3){
//        this.term1 = term1;
//        this.term2 = term2;
//        this.term3 = term3;
//    }

}
