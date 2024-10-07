package allcount.poc.credential.repository;

import allcount.poc.credential.entity.UserCredential;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository for the UserCredential entity.
 */
public interface UserCredentialRepository extends JpaRepository<UserCredential, UUID> {
    @Override
    Optional<UserCredential> findById(UUID uuid);

    Optional<UserCredential> findByUsername(String username);
}
