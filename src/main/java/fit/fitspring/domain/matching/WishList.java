package fit.fitspring.domain.matching;

import fit.fitspring.domain.BaseTimeEntity;
import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.trainer.Trainer;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="wish_list", uniqueConstraints={
        @UniqueConstraint(
                name = "cust_trainer",
                columnNames = {"cust_idx","trainer_idx"}
        )
})
public class WishList extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="wish_list_idx")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cust_idx")
    private Account customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_idx")
    private Trainer trainer;
}
