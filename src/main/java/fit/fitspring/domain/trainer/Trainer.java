package fit.fitspring.domain.trainer;

import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.matching.MatchingOrder;
import fit.fitspring.domain.review.Review;
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
@Table(name="trainer")
public class Trainer {

    @Id
    @Column(name="trainer_idx")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name="trainer_idx")
    private Account user;

    @Column(name = "trainer_sch")
    private String school;

    @Column(name = "trainer_major")
    private String major;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_idx")
    private Level level;

    @Column(name = "trainer_price_hour")
    private int priceHour;

    @Column(name = "trainer_price_add")
    private int priceAdd;

    @Column(name = "trainer_intro")
    private String intro;

    @Column(name = "trainer_service")
    private String service;

    @Enumerated(EnumType.STRING) @Column(name = "trainer_category")
    private Category category;

    @Column(name = "trainer_grade")
    private float grade;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "trainer")
    private UserImg userImg;

    @Builder.Default
    @OneToMany(mappedBy = "trainer")
    private List<Review> reviewList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "trainer")
    private List<TrainerCert> trainerCertList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "trainer")
    private List<MatchingOrder> matchingOrderList = new ArrayList<>();

    public void updateInfo(int priceHour, int priceAdd, String intro, String service){
        this.priceHour = priceHour;
        this.priceAdd = priceAdd;
        this.intro = intro;
        this.service = service;
    }
}