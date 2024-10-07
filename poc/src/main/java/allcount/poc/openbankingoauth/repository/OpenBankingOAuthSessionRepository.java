package allcount.poc.openbankingoauth.repository;

import allcount.poc.openbankingoauth.entity.OpenBankingOAuthSessionEntity;
import allcount.poc.openbankingoauth.object.OpenBankingOAuthSessionStatusEnum;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for OpenBankingOAuthSessionEntity.
 */
public interface OpenBankingOAuthSessionRepository extends JpaRepository<OpenBankingOAuthSessionEntity, UUID> {

    /**
     * Finds the OpenBankingOAuthSessionEntity by the state and status.
     *
     * @param state  - the state
     * @param status - the status
     * @return the OpenBankingOAuthSessionEntity
     */
    Optional<OpenBankingOAuthSessionEntity> findByStateAndStatus(UUID state,
                                                                 OpenBankingOAuthSessionStatusEnum status);
}
