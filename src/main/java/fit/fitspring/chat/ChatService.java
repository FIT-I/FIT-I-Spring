package fit.fitspring.chat;

import fit.fitspring.chat.dto.ChatRoomAndUserDto;
import fit.fitspring.chat.entity.ChatRoom;
import fit.fitspring.chat.entity.ChatRoomRepository;
import fit.fitspring.chat.entity.ChatUser;
import fit.fitspring.domain.account.Account;
import fit.fitspring.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {
    private final AccountService accountService;
    private final ChatRoomRepository chatRoomRepository;
    private Map<Long, ChatRoom> chatRooms;

    @PostConstruct
    //의존관게 주입완료되면 실행되는 코드
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    //채팅방 불러오기
    public List<ChatRoom> findAllRoom() {
        //채팅방 최근 생성 순으로 반환
        List<ChatRoom> result = new ArrayList<>(chatRooms.values());
        Collections.reverse(result);

        return result;
    }

    //채팅방 하나 불러오기
    public ChatRoom findById(Long roomId) {
        return chatRooms.get(roomId);
    }

    //채팅방 생성
    public ChatRoom createRoom(String name, List<String> emails) {
        ChatRoom chatRoom = new ChatRoom(name);
        List<Account> chatUsers = accountService.findAllByEmails(emails);
        List<ChatUser> users = new ArrayList<>();
        for (Account user : chatUsers) {
            ChatUser chatUser = ChatUser.of(chatRoom, user);
            users.add(chatUser);
        }
        chatRoom.setChatUser(users);
        chatRooms.put(chatRoom.getId(), chatRoom);

        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }

    public List<ChatRoomAndUserDto> findAllRoomsByUserId(Long userId) {
        Account account = accountService.findById(userId);
        return account.getChatUser().stream()
                .map(chatUser -> toEntity(chatUser.getChatRoom())).toList();
    }

    private ChatRoomAndUserDto toEntity(ChatRoom chatRoom){
        ChatRoomAndUserDto dto = ChatRoomAndUserDto.builder()
                .roomId(chatRoom.getId())
                .roomName(chatRoom.getRoomName())
                .emails(chatRoom.getChatUser().stream()
                        .map(cu ->
                                cu.getChatUser().getEmail())
                        .collect(Collectors.toList()))
                .build();
        return dto;
    }
}