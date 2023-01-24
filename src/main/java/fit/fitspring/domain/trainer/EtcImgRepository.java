package fit.fitspring.domain.trainer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtcImgRepository extends JpaRepository<EtcImg, Long> {

}
