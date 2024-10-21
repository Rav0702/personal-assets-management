package allcount.poc.cashaccount.repository;

import allcount.poc.cashaccount.entity.CashAccountEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the CashAccountEntity.
 */
public interface CashAccountRepository extends JpaRepository<CashAccountEntity, UUID> {

    /**
     * Find the CashAccountEntity by the IBAN.
     *
     * @param iban the IBAN
     * @return the CashAccountEntity
     */
    Optional<CashAccountEntity> findByIban(String iban);
}
