package fit.fitspring.chat;

import fit.fitspring.chat.dto.ChatRoomAndUserDto;
import fit.fitspring.chat.entity.*;
import fit.fitspring.domain.account.Account;
import fit.fitspring.exception.account.AccountNotFoundException;
import fit.fitspring.service.AccountService;
import fit.fitspring.utils.SecurityUtil;
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
    private final ChatBlockRepository chatBlockRepository;
    //private Map<Long, ChatRoom> chatRooms;

    /*
    @PostConstruct
    //의존관게 주입완료되면 실행되는 코드
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }
     */

    //채팅방 불러오기
    /*
    public List<ChatRoom> findAllRoom() {
        //채팅방 최근 생성 순으로 반환
        //List<ChatRoom> result = new ArrayList<>(chatRooms.values());
        Collections.reverse(result);
        return result;
    }
     */

    //채팅방 하나 불러오기
    public ChatRoom findById(Long roomId) {
        return chatRoomRepository.findById(roomId).orElse(null);
    }
    //채팅방 생성
    public ChatRoomAndUserDto createRoom(String name, List<String> emails) {
        ChatRoom chatRoom = new ChatRoom(name);
        List<Account> chatUsers = accountService.findAllByEmails(emails);
        if (chatUsers.isEmpty()){
            throw new AccountNotFoundException();
        }
        chatRoom.setChatUser(chatUsers.stream()
                .map(user -> ChatUser.of(chatRoom, user)).toList());

        //chatRooms.put(chatRoom.getId(), chatRoom);
        return toDto(chatRoomRepository.save(chatRoom));
    }

    public List<ChatRoomAndUserDto> findAllRoomsByUserId(Long userId) {
        Account account = accountService.findById(userId);
        List<Long> blockIds = account.getBlockUsers().stream()
                .map(block -> block.getSender().getId()).toList();
        return account.getChatUser().stream()
                .filter(cu -> {
                    List<Long> idInRoom = cu.getChatRoom().getChatUser().stream()
                            .map(ChatUser::getId).toList();
                    return !hasToBlock(blockIds, idInRoom);
                })
                .map(chatUser -> toDto(chatUser.getChatRoom())).toList();
    }

    private boolean hasToBlock(List<Long> blockCriteriaId, List<Long> userIdInRoom) {
        boolean containBlockId = blockCriteriaId.stream()
                .anyMatch(userIdInRoom::contains);
        return userIdInRoom.size() == 2 && containBlockId;
    }

    public ChatRoom getById(Long id){
        return chatRoomRepository.getReferenceById(id);
    }

    private ChatRoomAndUserDto toDto(ChatRoom chatRoom){
        ChatRoomAndUserDto dto = ChatRoomAndUserDto.builder()
                .roomId(chatRoom.getId() == null ? null : chatRoom.getId())
                .roomName(chatRoom.getRoomName())
                .emails(chatRoom.getChatUser().stream()
                        .map(cu ->
                                cu.getChatUser().getEmail())
                        .collect(Collectors.toList()))
                .build();
        return dto;
    }

    public void blockUser(Long blockId) {
        Long currentId = SecurityUtil.getCurrentAccountId();
        ChatBlock block = ChatBlock.builder()
                .receiver(accountService.getById(currentId))
                .sender(accountService.getById(blockId))
                .build();
        chatBlockRepository.save(block);
    }
}