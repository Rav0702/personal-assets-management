package allcount.poc.transaction.repository;

import allcount.poc.openbanking.embeddable.ExternalBankingIdEmbeddable;
import allcount.poc.transaction.entity.TransactionEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the TransactionEntity.
 */
public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {

    /**
     * Finds all transactions by their external banking ID.
     *
     * @param externalBankingId External banking ID
     * @return List of transaction entities with their external banking ID in the list.
     */
    List<TransactionEntity> findAllByExternalBankingIdIn(List<ExternalBankingIdEmbeddable> externalBankingId);

    /**
     * Finds transaction by its external banking ID.
     *
     * @param externalBankingId External banking ID
     * @return Transaction entity with its external banking ID equal to the given one. Returns empty optional if not found.
     */
    Optional<TransactionEntity> findByExternalBankingId(ExternalBankingIdEmbeddable externalBankingId);
}
