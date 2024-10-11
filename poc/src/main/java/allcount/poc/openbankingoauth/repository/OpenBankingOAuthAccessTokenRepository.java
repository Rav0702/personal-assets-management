package allcount.poc.openbankingoauth.repository;

import allcount.poc.openbankingoauth.entity.OpenBankingOAuthAccessTokenEntity;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the OpenBankingOAuthAccessTokenEntity.
 */
public interface OpenBankingOAuthAccessTokenRepository extends JpaRepository<OpenBankingOAuthAccessTokenEntity, UUID> {
    Optional<OpenBankingOAuthAccessTokenEntity> findFirstByUserIdOrderByStartDateTimeDesc(UUID userId);
}
