package fit.fitspring.controller.dto.trainer;

import fit.fitspring.domain.trainer.EtcImg;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Schema(title = "기타 사진 정보")
public class EtcImgList {
    @Schema(description = "사진의 idx", example = "1")
    private Long etcImgIdx;
    @Schema(description = "사진의 url", example = "https://fitibucket.s3.ap-northeast-2.amazonaws.com/ectImg/~~.jpg")
    private String etcImgLink;

    public EtcImgList(EtcImg entity){
        this.etcImgIdx = entity.getId();
        this.etcImgLink = entity.getEtcImg();
    }
}
