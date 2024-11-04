package allcount.poc.openbankingoauth.repository;

import allcount.poc.openbankingoauth.entity.OpenBankingRefreshTokenEntity;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the OpenBankingRefreshTokenEntity.
 */
public interface OpenBankingOAuthRefreshTokenRepository
        extends JpaRepository<OpenBankingRefreshTokenEntity, UUID> {

    /**
     * Finds the most recent refresh token by user ID and bank.
     *
     * @param userId - the user ID
     * @param bank   - the bank
     * @return the OpenBankingRefreshTokenEntity
     */
    Optional<OpenBankingRefreshTokenEntity> findFirstByUserIdAndBankOrderByCreatedTimestampDesc(UUID userId,
                                                                                                OpenBankingBankEnum bank);

    /**
     * Checks if the refresh token exists by user ID and bank.
     *
     * @param id                  - the user ID
     * @param openBankingBankEnum - the bank
     * @return true if the refresh token exists, false otherwise
     */
    boolean existsByUserIdAndBank(UUID id, OpenBankingBankEnum openBankingBankEnum);
}
