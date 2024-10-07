package allcount.poc.user.repository;

import allcount.poc.user.entity.AllcountUserDetailsEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the UserDetails entity.
 */
public interface UserDetailsRepository extends JpaRepository<AllcountUserDetailsEntity, UUID> {
}
