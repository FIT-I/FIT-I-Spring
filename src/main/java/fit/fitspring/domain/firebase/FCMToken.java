package fit.fitspring.domain.firebase;

import fit.fitspring.domain.account.Account;
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

    @OneToOne
    private Account account;
}
