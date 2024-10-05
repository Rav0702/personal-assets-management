package allcount.poc.credential.repository;

import allcount.poc.credential.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserCredentialRepository extends JpaRepository<UserCredential, UUID> {
    @Override
    Optional<UserCredential> findById(UUID uuid);

    Optional<UserCredential> findByUsername(String username);
}
