package fit.fitspring.domain.account;

import fit.fitspring.domain.matching.MatchingOrder;
import fit.fitspring.domain.matching.WishList;
import fit.fitspring.domain.review.Review;
import fit.fitspring.domain.trainer.Trainer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@Getter
@Setter
@Entity
@Table(name="user",
        indexes = {
            @Index(name = "email", columnList = "user_email"),
            @Index(name = "userIdx", columnList = "user_idx")
        }
)
public class Account {
    @Column(name="user_idx")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, name = "user_email")
    private String email;

    @Column(name="user_name")
    private String name;

    @Column(name="user_pwd")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private AccountType accountType;

    @Column(name="user_status")
    private String status;

    @Builder.Default
    @OneToMany(mappedBy = "customer")
    private List<Review> reviewList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "customer")
    private List<MatchingOrder> matchingOrderList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "customer")
    private List<WishList> wishListList = new ArrayList<>();

    @ColumnDefault("off")
    @Column(name="user_alarm_state")
    private String alarmState;

    @ColumnDefault("A")
    @Column(name="user_state")
    private String userState;

    @Column(name="user_location")
    private String location;

    public void updateName(String name){
        this.name=name;
    }
    public void modifyLocation(String location){ this.location = location; }
}
