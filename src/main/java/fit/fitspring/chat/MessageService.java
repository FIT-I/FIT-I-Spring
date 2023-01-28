package fit.fitspring.chat;

import fit.fitspring.chat.entity.*;
import fit.fitspring.controller.dto.firebase.FcmMessage;
import fit.fitspring.service.AccountService;
import fit.fitspring.service.FirebaseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {
    private final ChatService chatService;
    private final AccountService accountService;
    private final FirebaseService firebaseService;
    private final SimpMessageSendingOperations sendingOperations;
    private final MessageRepository messageRepository;

    public List<MessageDto> findAfterOf(Long roomId, Long messageId) {
        List<Message> ret = messageRepository.findByRoomIdAndAfterMessageId(roomId, messageId);
        return ret.stream().map(this::toDto).toList();
    }

    @Transactional
    public void sendMessage(ChatMessageDto message) throws IOException {
        ChatRoom chatRoom = chatService.findById(message.getRoomId());
        sendToSocket(message); // socket 전송
        messageRepository.save(toEntity(message, chatRoom)); // db 저장
        sendToAlert(message, chatRoom);
    }
    private void sendToAlert(ChatMessageDto message, ChatRoom chatRoom) throws IOException {
        List<String> tokens = chatRoom.getChatUser().stream()
                .filter(cu ->cu.getChatUser().getFcmToken() != null)
                .map(cu -> cu.getChatUser().getFcmToken().getToken()).toList();
        for (String token : tokens){
            FcmMessage fcmMessage = FcmMessage.builder()
                    .message(
                            FcmMessage.Message.builder()
                                    .token(token)
                                    .notification(new FcmMessage.Notification(
                                            "채팅이 왔어요",
                                            message.getMessage(),
                                            null
                                    ))
                                    .build()
                    )
                    .build();
            firebaseService.sendMessage(fcmMessage);
        }

    }

    private void sendToSocket(ChatMessageDto message){
        sendingOperations.convertAndSend("/topic/chat/room/"+message.getRoomId(),message);
    }

    private Message toEntity(ChatMessageDto message, ChatRoom chatRoom) {
        return Message.builder()
                .data(message.getMessage())
                .chatRoom(chatRoom)
                .sender(accountService.getById(message.getSenderId()))
                .build();
    }
    private MessageDto toDto(Message message){
        return MessageDto.builder()
                .id(message.getId())
                .data(message.getData())
                .chatRoomId(message.getChatRoom().getId())
                .senderId(message.getSender().getId())
                .email(message.getSender().getEmail())
                .createdAt(message.getCreatedAt())
                .build();
    }
}