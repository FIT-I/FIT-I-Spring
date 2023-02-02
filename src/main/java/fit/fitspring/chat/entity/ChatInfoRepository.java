package fit.fitspring.chat.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatInfoRepository extends JpaRepository<ChatInfo, Long> {

}
