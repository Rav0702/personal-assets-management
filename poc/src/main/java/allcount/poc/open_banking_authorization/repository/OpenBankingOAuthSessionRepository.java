package allcount.poc.open_banking_authorization.repository;

import allcount.poc.open_banking_authorization.entity.OpenBankingOAuthSessionEntity;
import allcount.poc.open_banking_authorization.object.OpenBankingOAuthSessionStatusEnum;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenBankingOAuthSessionRepository extends JpaRepository<OpenBankingOAuthSessionEntity, UUID> {

    public Optional<OpenBankingOAuthSessionEntity> findByStateAndStatus(UUID state, OpenBankingOAuthSessionStatusEnum status);
}
