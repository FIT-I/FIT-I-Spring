package fit.fitspring.controller.dto.communal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Schema(title = "공지사항")
public class AnnouncementDto {

    @Schema(description = "공지사항 제목", example = "업데이트 안내")
    private String title;
    @Schema(description = "공지사항 내용", example = "안녕하세요")
    private String contents;
    @Schema(description = "최근 업데이트날짜", example = "2023.01.10")
    private LocalDate createdAt;

    public AnnouncementDto(String title, String contents, LocalDate createdAt) {
        this.title = title;
        this.contents = contents;
        this.createdAt = createdAt;
    }
}