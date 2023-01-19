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
@Table(name="level")
public class Level {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="level_idx")
    private Long id;

    @Column(unique = true, name = "level_name")
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "level")
    private List<Trainer> trainers = new ArrayList<>();
}
