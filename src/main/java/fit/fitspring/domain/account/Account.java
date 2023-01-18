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
    @Column(name="user_idx")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_name")
    private String name;

    @Column(name="user_email", unique = true)
    private String email;

    @Column(name="user_pwd")
    private String password;

    @Column(name="user_role")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

}
