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
@Table(name="matching_order", uniqueConstraints={
        @UniqueConstraint(
                name = "cust_trainer",
                columnNames = {"cust_idx","trainer_idx"}
        )
})
public class MatchingOrder extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_idx")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cust_idx")
    private Account customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_idx")
    private Trainer trainer;

    @Column(name= "matching_start_at")
    private String startAt;

    @Column(name= "matching_finish_at")
    private String finishAt;

    @Enumerated(EnumType.STRING) @Column(name = "order_pickup")
    private PickUpType pickUpType;

    @Column(name = "is_complete")
    private String isComplete;
}
