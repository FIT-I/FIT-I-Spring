package fit.fitspring.domain.redBell;

import fit.fitspring.domain.BaseTimeEntity;
import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.trainer.Category;
import fit.fitspring.domain.trainer.Trainer;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="redbell")
public class RedBell extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="redbell_idx")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cust_idx")
    private Account customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_idx")
    private Trainer trainer;

    @Enumerated(EnumType.STRING) @Column(name = "redbell_reason")
    private ReasonForRedBell reason;

    @Column(name = "redbell_target")
    private String target;

}


