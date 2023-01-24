package fit.fitspring.domain.trainer;

import fit.fitspring.domain.account.Account;
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
@Table(name="user_img")
public class UserImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_img_idx")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_idx")
    private Trainer trainer;

    @Column(name = "profile_img_path")
    private String profile;

    @Column(name = "pack_img_path")
    private String backGround;

    @Builder.Default
    @OneToMany(mappedBy = "userImg")
    private List<EtcImg> etcImgList = new ArrayList<>();

    public void modifyProfile(String profile){ this.profile = profile; }
}
