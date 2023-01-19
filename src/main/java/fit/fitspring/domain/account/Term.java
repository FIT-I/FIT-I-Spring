package fit.fitspring.domain.account;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="term")
public class Term {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="term_idx")
    private Long id;

    @Column(unique = true, name = "term_name")
    private String name;

    @Column(name = "term_detail")
    private String detail;

    @Column(name = "term_required")
    private String required;
}
