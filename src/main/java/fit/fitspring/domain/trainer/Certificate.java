package fit.fitspring.domain.trainer;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="certificate")
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cert_idx")
    private Long id;

    @Column(unique = true, name = "cert_name")
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "certificate")
    private List<TrainerCert> trainerCertList = new ArrayList<>();
}
