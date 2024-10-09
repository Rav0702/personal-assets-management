package allcount.poc.user.repository;

import allcount.poc.user.entity.AllcountUser;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for the AllcountUser entity.
 */
public interface AllcountUserRepository extends JpaRepository<AllcountUser, UUID> {

    /**
     * Find user by id and fetch userCredential. (For testing purposes only)
     *
     * @param userId userId
     * @return user
     */
    @Query("SELECT u FROM AllcountUser u JOIN FETCH u.userCredential WHERE u.id = :userId")
    Optional<AllcountUser> findByIdWithUserCredentialForTestingOnly(@Param("userId") UUID userId);

    /**
     * Find user by username.
     *
     * @param username username
     * @return user
     */
    @Query("SELECT u FROM AllcountUser u JOIN FETCH u.userCredential WHERE u.userCredential.username = :username")
    Optional<AllcountUser> findByUsername(String username);
}
