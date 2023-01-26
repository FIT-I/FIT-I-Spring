package fit.fitspring.chat.entity;

import fit.fitspring.domain.account.Account;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(indexes = {
        @Index(name = "idx_room_message", columnList = "room_id, id")
})
@EntityListeners({AuditingEntityListener.class})
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String data;
    @ManyToOne @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;
    @ManyToOne @JoinColumn(name = "account_id")
    private Account sender;
    @CreatedDate
    private LocalDateTime createdAt;


}
