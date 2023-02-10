package fit.fitspring.controller.dto.communal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(title = "매칭 장소 주소 카카오 우편 번호 API 반환값")
public class ZipCodeDto {

    @Schema(description = "우편 번호", example = "15215")
    private String zipCode;
    @Schema(description = "도로명 주소", example = "")
    private String streetAddress;
    @Schema(description = "상세 주소", example = "")
    private String detailAddress;
    @Schema(description = "참고 항목", example = "(흑석동)")
    private String extraAddress;

    public ZipCodeDto(){
        this.zipCode = "";
        this.streetAddress = "";
        this.detailAddress = "";
        this.extraAddress = "";
    }

    public ZipCodeDto(String zipCode, String streetAddress, String detailAddress, String extraAddress){
        this.zipCode = zipCode;
        this.streetAddress = streetAddress;
        this.detailAddress = detailAddress;
        this.extraAddress = extraAddress;
    }

}
