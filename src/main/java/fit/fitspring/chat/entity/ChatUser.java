package fit.fitspring.chat.entity;

import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.account.AccountType;
import jakarta.persistence.*;
import lombok.*;

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

    public static ChatUser of(ChatRoom room, Account user){
        ChatUser chatToUser = new ChatUser();
        chatToUser.setChatRoom(room);
        chatToUser.setChatUser(user);
        return chatToUser;
    }

}
