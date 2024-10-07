package allcount.poc.user.repository;

import allcount.poc.user.entity.AllcountUserDetailsEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the UserDetails entity.
 */
public interface UserDetailsRepository extends JpaRepository<AllcountUserDetailsEntity, UUID> {

    /**
     * Find user by username.
     *
     * @param username username
     * @return user
     */
    Optional<AllcountUserDetailsEntity> findByUsername(String username); //

}
