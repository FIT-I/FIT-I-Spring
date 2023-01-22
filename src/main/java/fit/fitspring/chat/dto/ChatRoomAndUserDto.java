package fit.fitspring.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ChatRoomAndUserDto {
    private Long roomId;
    private String roomName;
    private List<String> emails;
}
