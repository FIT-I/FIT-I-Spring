package fit.fitspring.controller.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(title = "트레이너매칭요청")
public class MatchingRequestDto {
    @Schema(description = "서비스명", example = "식단관리")
    private String category;
    @Schema(description = "매칭시작일", example = "2023.01.11")
    private String startAt;
    @Schema(description = "매칭종료일", example = "2023.01.11")
    private String finishAt;
    @Schema(description = "픽업형태", example = "제가직접갈게요")
    private String type;

    public MatchingRequestDto(String category, String startAt,  String finishAt, String type) {
        this.category = category;
        this.startAt = startAt;
        this.finishAt = finishAt;
        this.type = type;
    }
}