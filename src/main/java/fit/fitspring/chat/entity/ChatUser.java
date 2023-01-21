package fit.fitspring.chat.entity;

import fit.fitspring.domain.account.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ChatUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;
    @ManyToOne @JoinColumn(name = "account_id")
    private Account chatUser;
}
