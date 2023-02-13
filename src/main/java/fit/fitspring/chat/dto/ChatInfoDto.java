package fit.fitspring.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
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
    private String customerName;
    private String pickUp;
    private String customerLocation;

    private LocalDate createdAt;

    private Long matchingId;
    private String trainerProfile;
    private String trainerLocation;
    private String customerProfile;
}
