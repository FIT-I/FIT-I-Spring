package fit.fitspring.domain.account;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="user",
        indexes = {
            @Index(name = "email", columnList = "user_email"),
            @Index(name = "userIdx", columnList = "user_idx")
        }
)
public class Account {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_name")
    private String name;

    @Column(name="user_email", unique = true)
    private String email;

    @Column(name="user_pwd")
    private String password;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

}
