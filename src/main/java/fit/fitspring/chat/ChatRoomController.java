package fit.fitspring.chat;

import fit.fitspring.chat.dto.ChatRoomAndUserDto;
import fit.fitspring.chat.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatService chatService;

    // 채팅 리스트 화면
    @GetMapping("/room")
    public String rooms(Model model) {
        return "/chat/room";
    }

    // 사용자의 채팅방 목록 반환
    @GetMapping("/rooms/users/{userId}")
    @ResponseBody
    public List<ChatRoomAndUserDto> room(@PathVariable Long userId) {
        List<ChatRoomAndUserDto> ret = chatService.findAllRoomsByUserId(userId);
        return ret;
    }
    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ChatRoomAndUserDto createRoom(@RequestBody ChatRoomAndUserDto dto) {
        return chatService.createRoom(dto.getRoomName(), dto.getEmails());
    }
    // 채팅방 입장 화면
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }
    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable Long roomId) {
        return chatService.findById(roomId);
    }
}