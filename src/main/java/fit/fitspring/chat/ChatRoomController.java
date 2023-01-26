package fit.fitspring.chat;

import fit.fitspring.chat.dto.ChatRoomAndUserDto;
import fit.fitspring.chat.entity.ChatRoom;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Tag(name = "채팅 API")
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatService chatService;

    // 채팅 리스트 화면 - test용 view
    @GetMapping("/room")
    public String rooms(Model model) {
        return "/chat/room";
    }

    // 사용자의 채팅방 목록 반환
    @Operation(summary = "유저의 채팅방 조회", description = "해당 유저가 참여한 채팅방 목록 조회")
    @GetMapping("/rooms/users/{userId}")
    @ResponseBody
    public List<ChatRoomAndUserDto> room(@Parameter(description = "유저 식별자") @PathVariable Long userId) {
        List<ChatRoomAndUserDto> ret = chatService.findAllRoomsByUserId(userId);
        return ret;
    }
    // 채팅방 생성
    @Operation(summary = "채팅방 생성", description = "채팅방 생성 ")
    @PostMapping("/room")
    @ResponseBody
    public ChatRoomAndUserDto createRoom(@Parameter(description = "채팅방 이름, 참여자의 email")@RequestBody ChatRoomAndUserDto dto) {
        return chatService.createRoom(dto.getRoomName(), dto.getEmails());
    }
    // 채팅방 입장 화면 - test view
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }
    // 특정 채팅방 조회
    @Operation(summary = "단일 채팅방 조회", description = "단일 채팅방 조회")
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@Parameter(description = "채팅방 id")@PathVariable Long roomId) {
        return chatService.findById(roomId);
    }
}