package fit.fitspring.controller.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Schema(title = "트레이너매칭요청")
public class MatchingRequestDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "매칭시작일(yyyy-MM-dd 포맷으로 넘겨주세요)", example = "2023-01-25")
    private LocalDate startAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "매칭종료일(yyyy-MM-dd 포맷으로 넘겨주세요)", example = "2023-01-30")
    private LocalDate finishAt;
    @Schema(description = "픽업형태(CUSTOMER_GO / TRAINER_GO)", example = "CUSTOMER_GO")
    private String type;

    public MatchingRequestDto(LocalDate startAt, LocalDate finishAt, String type) {
        this.startAt = startAt;
        this.finishAt = finishAt;
        this.type = type;
    }
}