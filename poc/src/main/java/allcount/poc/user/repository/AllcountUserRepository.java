package allcount.poc.user.repository;

import allcount.poc.user.entity.AllcountUser;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the AllcountUser entity.
 */
public interface AllcountUserRepository extends JpaRepository<AllcountUser, UUID> {
}
