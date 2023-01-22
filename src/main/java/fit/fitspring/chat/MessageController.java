package fit.fitspring.chat;

import fit.fitspring.chat.entity.ChatMessageDto;
import fit.fitspring.chat.entity.MessageDto;
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
public class MessageController {

    private final MessageService messageService;

    @MessageMapping("/chat/message")
    public void enter(ChatMessageDto message) throws IOException {
        messageService.sendMessage(message);
    }

    @GetMapping("/chat/rooms/{roomId}/messages/{messageId}")
    public List<MessageDto> getMessagesAfterOf(@PathVariable Long roomId,
                                               @PathVariable Long messageId) {
        return messageService.findAfterOf(roomId, messageId);
    }
}