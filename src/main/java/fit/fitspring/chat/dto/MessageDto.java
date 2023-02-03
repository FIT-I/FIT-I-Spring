package fit.fitspring.chat.dto;

import fit.fitspring.domain.account.Account;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
    private Long id;
    private String data;
    private Long chatRoomId;
    private Long senderId;
    private String email;
    private LocalDateTime createdAt;


}
