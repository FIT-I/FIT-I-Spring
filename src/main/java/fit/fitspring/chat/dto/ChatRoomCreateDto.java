package fit.fitspring.chat.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatRoomCreateDto {
    private String roomName;
    private List<String> emails;
}
