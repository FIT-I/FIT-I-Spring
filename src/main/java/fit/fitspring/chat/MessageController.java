package fit.fitspring.chat;

import fit.fitspring.chat.entity.ChatMessageDto;
import fit.fitspring.chat.entity.MessageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "채팅 API")
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "채팅 전송",
    description = "1. socket 연결은  /fit/chat  로 해야합니다.\n" +
            "2. 메세지 전송도 /chat/message 가 아니라 /fit/chat/message 로 전송해야 합니다. \n" +
            "3. 자세한 사용 방법은 /resources/templates/chat/roomdetail.html 의 script를 확인해주세요. pub/sub 모델입니다."
    )
    @MessageMapping("/chat/message")
    public void enter(ChatMessageDto message) throws IOException {
        messageService.sendMessage(message);
    }
    @Operation(summary = "채팅 내역 fetch", description = "대상 roomId 의 채팅 내역 중 messageId 다음 것을 fetch")
    @GetMapping("/chat/rooms/{roomId}/messages/{messageId}")
    public List<MessageDto> getMessagesAfterOf(@PathVariable Long roomId,
                                               @Parameter(description = "마지막으로 받은 메세지 Id")@PathVariable Long messageId) {
        return messageService.findAfterOf(roomId, messageId);
    }
}