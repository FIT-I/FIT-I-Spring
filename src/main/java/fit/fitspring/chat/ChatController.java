package fit.fitspring.chat;

import fit.fitspring.chat.dto.ChatRoomAndUserDto;
import fit.fitspring.chat.entity.ChatMessageDto;
import fit.fitspring.chat.entity.ChatRoom;
import fit.fitspring.chat.entity.MessageDto;
import fit.fitspring.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@Tag(name = "채팅 API")
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private final MessageService messageService;

    // 사용자의 채팅방 목록 반환
    @Operation(summary = "유저의 채팅방 조회", description = "해당 유저가 참여한 채팅방 목록 조회")
    @GetMapping("/rooms/users/{userId}")
    public List<ChatRoomAndUserDto> room(@Parameter(description = "유저 식별자") @PathVariable Long userId) {
        List<ChatRoomAndUserDto> ret = chatService.findAllRoomsByUserId(userId);
        return ret;
    }
    // 채팅방 생성
    @Operation(summary = "채팅방 생성", description = "채팅방 생성 ")
    @PostMapping("/room")
    public ChatRoomAndUserDto createRoom(@Parameter(description = "채팅방 이름, 참여자의 email")@RequestBody ChatRoomAndUserDto dto) {
        return chatService.createRoom(dto.getRoomName(), dto.getEmails());
    }

    @Operation(summary = "단일 채팅방 조회", description = "단일 채팅방 조회")
    @GetMapping("/room/{roomId}")
    public ChatRoom roomInfo(@Parameter(description = "채팅방 id")@PathVariable Long roomId) {
        return chatService.findById(roomId);
    }

    @Operation(summary = "채팅 전송",
            description = "1. socket 연결은  /fit/chat  로 해야합니다.\n" +
                    "2. 메세지 전송도 /chat/message 가 아니라 /fit/chat/message 로 전송해야 합니다. \n" +
                    "3. 자세한 사용 방법은 /resources/templates/chat/roomdetail.html 의 script를 확인해주세요. pub/sub 모델입니다."
    )
    @MessageMapping("/chat/message")
    public void sendMessage(ChatMessageDto message) throws IOException {
        messageService.sendMessage(message);
    }

    @Operation(summary = "채팅 내역 fetch", description = "대상 roomId 의 채팅 내역 중 messageId 다음 것을 fetch")
    @GetMapping("/chat/rooms/{roomId}/messages/{messageId}")
    public List<MessageDto> getMessagesAfterOf(@PathVariable Long roomId,
                                               @Parameter(description = "마지막으로 받은 메세지 Id")@PathVariable Long messageId) {
        return messageService.findAfterOf(roomId, messageId);
    }

    @Operation(summary = "메시지 차단 (미완)")
    @PostMapping("/block/{userId}")
    public BaseResponse<String> blockUser(@Parameter(description = "대상 유저의 메세지를 차단합니다")@PathVariable Long userId) {
        //chatService.blockUser(userId);
        return new BaseResponse<String>("해당 유저 차단에 성공했습니다.");
    }
}