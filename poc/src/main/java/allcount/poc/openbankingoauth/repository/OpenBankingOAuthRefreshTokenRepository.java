package allcount.poc.openbankingoauth.repository;

import allcount.poc.openbankingoauth.entity.OpenBankingOAuthRefreshTokenEntity;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the OpenBankingOAuthRefreshTokenEntity.
 */
public interface OpenBankingOAuthRefreshTokenRepository
        extends JpaRepository<OpenBankingOAuthRefreshTokenEntity, UUID> {

    /**
     * Finds the most recent refresh token by user ID and bank.
     *
     * @param userId - the user ID
     * @param bank   - the bank
     * @return the OpenBankingOAuthRefreshTokenEntity
     */
    Optional<OpenBankingOAuthRefreshTokenEntity> findFirstByUserIdAndBankOrderByCreatedTimestampDesc(UUID userId,
                                                                                                     OpenBankingBankEnum bank);

    /**
     * Checks if the refresh token exists by user ID and bank.
     *
     * @param id                 - the user ID
     * @param openBankingBankEnum - the bank
     * @return true if the refresh token exists, false otherwise
     */
    boolean existsByUserIdAndBank(UUID id, OpenBankingBankEnum openBankingBankEnum);
}
