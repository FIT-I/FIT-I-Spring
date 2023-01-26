package fit.fitspring.domain.account;

import fit.fitspring.chat.entity.ChatUser;
import fit.fitspring.chat.entity.Message;
import fit.fitspring.domain.BaseTimeEntity;
import fit.fitspring.domain.firebase.FCMToken;
import fit.fitspring.domain.matching.MatchingOrder;
import fit.fitspring.domain.matching.WishList;
import fit.fitspring.domain.review.Review;
import fit.fitspring.domain.trainer.Trainer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
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
public class Account extends BaseTimeEntity implements UserDetails {
    @Column(name="user_idx")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Trainer trainer;

    @Column(unique = true, name = "user_email")
    private String email;

    @Column(name="user_name")
    private String name;

    @Column(name="user_pwd")
    private String password;

    @ColumnDefault("customerProfile1")
    @Column(name = "user_profile")
    private String profile;

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

    @OneToMany(mappedBy = "chatUser")
    private List<ChatUser> chatUser =  new ArrayList<>();
    @OneToMany(mappedBy = "sender")
    private List<Message> message =  new ArrayList<>();

    @OneToOne(mappedBy = "account")
    private FCMToken fcmToken;
    public void updateName(String name){
        this.name=name;
    }
    public void modifyLocation(String location){ this.location = location; }
    public void modifyProfile(String profile){ this.profile = profile; }
    public void modifyMatchingState(String status){this.userState = status;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
