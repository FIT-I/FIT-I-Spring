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
@Table(name="announcement")
public class Announcement extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="anno_idx")
    private Long id;

    @Column(unique = true, name = "anno_title")
    private String title;

    @Column(name = "anno_content")
    private String content;

}
