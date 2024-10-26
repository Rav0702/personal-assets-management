package allcount.poc.openbankingoauth.repository;

import allcount.poc.openbankingoauth.entity.OpenBankingSessionEntity;
import allcount.poc.openbankingoauth.object.enums.OpenBankingOAuthSessionStatusEnum;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for OpenBankingSessionEntity.
 */
public interface OpenBankingOAuthSessionRepository extends JpaRepository<OpenBankingSessionEntity, UUID> {

    /**
     * Finds the OpenBankingSessionEntity by the state and status.
     *
     * @param state  - the state
     * @param status - the status
     * @return the OpenBankingSessionEntity
     */
    Optional<OpenBankingSessionEntity> findByStateAndStatus(UUID state, OpenBankingOAuthSessionStatusEnum status);

    /**
     * Finds the OpenBankingSessionEntity by the user id and status.
     *
     * @param userId - the user id
     * @param status - the status
     * @return the OpenBankingSessionEntity
     */
    List<OpenBankingSessionEntity> findByUserIdAndStatus(UUID userId, OpenBankingOAuthSessionStatusEnum status);
}
