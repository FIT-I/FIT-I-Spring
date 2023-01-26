package fit.fitspring.domain.review;

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
@Table(name="review", uniqueConstraints={
        @UniqueConstraint(
                name = "cust_trainer",
                columnNames = {"cust_idx","trainer_idx"}
        )
})
public class Review extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="review_idx")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cust_idx")
    private Account customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_idx")
    private Trainer trainer;

    @Column(name = "review_content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "review_grade")
    private int grade;

    @Column(name = "is_deleted")
    private int isDeleted;

}

