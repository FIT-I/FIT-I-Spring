package fit.fitspring.domain.trainer;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="user_ect_img")
public class EtcImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="etc_img_idx")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_img_idx")
    private UserImg userImg;

    @Column(name="etc_img")
    String etcImg;
}
