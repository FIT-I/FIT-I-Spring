package fit.fitspring.domain.firebase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FirebaseRepository extends JpaRepository<FCMToken, Long> {
    Optional<FCMToken> findByUserEmail(String email);
}
