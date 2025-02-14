package allcount.poc.openbankingoauth.repository;

import allcount.poc.openbankingoauth.entity.OpenBankingOAuthAccessTokenRedisEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the OpenBankingOAuthAccessTokenRedisEntity.
 */
@Repository
public interface OpenBankingOAuthAccessTokenRedisRepository
        extends CrudRepository<OpenBankingOAuthAccessTokenRedisEntity, String> {
}
