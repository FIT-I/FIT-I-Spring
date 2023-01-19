package fit.fitspring.domain.account;

import fit.fitspring.domain.firebase.FCMToken;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Account {
    @Id
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @OneToOne(mappedBy = "account")
    private FCMToken token;

}
