package fit.fitspring.domain.account;

import fit.fitspring.domain.matching.MatchingOrder;
import fit.fitspring.domain.matching.WishList;
import fit.fitspring.domain.review.Review;
import fit.fitspring.domain.trainer.Trainer;
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
@Table(name="user",
        indexes = @Index(
                name = "idx_email",
                columnList = "user_email"
        )
)
public class Account{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_idx")
    private Long id;

    @Column(unique = true, name = "user_email")
    private String email;
    @Column(name = "user_pwd")
    private String password;
    @Column(name = "user_name")
    private String name;

    @Enumerated(EnumType.STRING) @Column(name = "user_role")
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

    public void updateName(String name){
        this.name=name;
    }
}
