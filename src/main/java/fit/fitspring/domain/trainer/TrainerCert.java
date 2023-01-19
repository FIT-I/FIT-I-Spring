package fit.fitspring.domain.trainer;

import fit.fitspring.domain.account.TermAgreeId;
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
@IdClass(TrainerCert.class)
@Table(name="trainer_cert",
        uniqueConstraints={
                @UniqueConstraint(
                        name="trainer_cert",
                        columnNames = {"trainer_idx","cert_idx"}
                )
        })
public class TrainerCert implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_idx")
    private Trainer trainer;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cert_idx")
    private Certificate certificate;

}
