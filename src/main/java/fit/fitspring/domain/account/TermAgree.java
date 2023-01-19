package fit.fitspring.domain.account;

import fit.fitspring.domain.BaseTimeEntity;
import fit.fitspring.domain.trainer.Certificate;
import fit.fitspring.domain.trainer.Trainer;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode
@IdClass(TermAgreeId.class)
@Table(name="term_agree",
        uniqueConstraints={
                @UniqueConstraint(
                        name="user_term",
                        columnNames = {"user_idx","term_idx"}
                )
        })
public class TermAgree extends BaseTimeEntity implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private Account user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_idx")
    private Term term;

    @Column(name = "agree")
    private String agree;

}
