package fit.fitspring.chat.entity;

import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.trainer.Trainer;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners({AuditingEntityListener.class})
public class ChatInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String link;

    @ManyToOne @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @ManyToOne @JoinColumn(name = "customer_id")
    private Account customer;

    @CreatedDate
    private LocalDateTime createdAt;


}
