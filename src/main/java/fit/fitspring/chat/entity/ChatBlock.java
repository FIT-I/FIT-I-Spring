package fit.fitspring.chat.entity;

import fit.fitspring.domain.account.Account;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@EntityListeners({AuditingEntityListener.class})
public class ChatBlock implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Indexed
    @ManyToOne @JoinColumn(name = "sender_id")
    private Account receiver;
    @ManyToOne
    private Account sender;

    @CreatedDate
    private LocalDateTime createdAt;
}