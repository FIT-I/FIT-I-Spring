package fit.fitspring.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ChatInfoDto {

    private String openChatLink;
    private Long trainerId;
    private String trainerName;
    private Float trainerGrade;
    private String trainerSchool;

    private Long customerId;
    private String pickUp;
    private String customerLocation;

    private LocalDateTime createdAt;
}
