package fit.fitspring.domain.trainer;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="trainer_cert",
        uniqueConstraints={
                @UniqueConstraint(
                        name="trainer_cert",
                        columnNames = {"trainer_idx","cert_idx"}
                )
        })
public class Trainer_Cert {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="trainer_cert_idx")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_idx")
    private Trainer trainer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cert_idx")
    private Certificate certificate;

}
