package fit.fitspring.domain.redBell;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedBellRepository extends JpaRepository<RedBell, Long> {
}
