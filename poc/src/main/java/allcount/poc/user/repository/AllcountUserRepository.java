package allcount.poc.user.repository;

import org.springframework.data.repository.CrudRepository;
import allcount.poc.user.entity.AllcountUser;

import java.util.UUID;

public interface AllcountUserRepository extends CrudRepository<AllcountUser, UUID> {
}
