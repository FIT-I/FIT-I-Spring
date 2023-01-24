package fit.fitspring.domain.account;

import fit.fitspring.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="school")
public class School {

    @Id
    @Column(name = "sch_name")
    private String name;

    @Column(unique = true, name = "sch_email")
    private String email;

}