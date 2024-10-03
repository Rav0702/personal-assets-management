package allcount.poc.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import allcount.poc.user.entity.AllcountUser;

import java.util.UUID;

public interface AllcountUserRepository extends JpaRepository<AllcountUser, UUID> {
}
