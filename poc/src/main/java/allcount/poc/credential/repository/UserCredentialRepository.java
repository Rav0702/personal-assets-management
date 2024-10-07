package allcount.poc.credential.repository;

import allcount.poc.credential.entity.UserCredential;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the UserCredential entity.
 */
public interface UserCredentialRepository extends JpaRepository<UserCredential, UUID> {
    @Override
    Optional<UserCredential> findById(UUID uuid);

    /**
     * Find user by username.
     *
     * @param username username
     * @return user
     */
    Optional<UserCredential> findByUsername(String username);
}
