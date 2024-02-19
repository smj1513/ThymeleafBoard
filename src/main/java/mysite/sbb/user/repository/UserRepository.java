package mysite.sbb.user.repository;

import mysite.sbb.user.domain.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {

	Optional<SiteUser> findByUserIdAndPassword(String userId, String Password);

	Optional<SiteUser> findByUserId(String userId);


}
