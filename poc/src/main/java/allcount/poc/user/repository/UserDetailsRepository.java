package allcount.poc.user.repository;

import allcount.poc.credential.entity.UserCredential;
import allcount.poc.user.entity.AllcountUserDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserDetailsRepository extends JpaRepository<AllcountUserDetailsEntity, UUID> {

    Optional<AllcountUserDetailsEntity> findByUsername(String username); //

}
