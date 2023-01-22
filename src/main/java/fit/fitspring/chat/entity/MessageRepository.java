package fit.fitspring.chat.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value = "select m " +
            "from Message m " +
            "where m.chatRoom.id = :roomId AND m.id > :messageId")
    List<Message> findByRoomIdAndAfterMessageId(@Param("roomId") Long roomId,
                             @Param("messageId") Long messageId);

}
