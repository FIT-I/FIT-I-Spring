package fit.fitspring.chat;

import fit.fitspring.chat.entity.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @MessageMapping("/chat/message")
    public void enter(ChatMessageDto message) throws IOException {
        messageService.sendMessage(message);
    }
}