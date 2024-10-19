package allcount.poc.transaction.repository;

import allcount.poc.transaction.entity.TransactionEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the TransactionEntity.
 */
public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {

}
