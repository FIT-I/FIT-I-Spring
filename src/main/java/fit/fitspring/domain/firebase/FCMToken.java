package fit.fitspring.domain.firebase;

import fit.fitspring.domain.account.AccountType;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class FCMToken {
    @Id
    private String token;
    @Column(unique = true)
    private String user_email;
}
