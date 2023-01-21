package fit.fitspring.chat.entity;

import fit.fitspring.domain.account.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ChatRoom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomName;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatUser> chatUser = new ArrayList<>();

    public static ChatRoom create(String name) {
        ChatRoom room = new ChatRoom();
        room.roomName = name;
        return room;
    }
}