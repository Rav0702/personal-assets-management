package allcount.poc.open_banking_authorization.repository;

import allcount.poc.open_banking_authorization.entity.OpenBankingOAuthAccessTokenEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenBankingOAuthAccessTokenRepository extends JpaRepository<OpenBankingOAuthAccessTokenEntity, UUID> {
}
