package allcount.poc.openbankingoauth.service;

import allcount.poc.openbankingoauth.entity.OpenBankingOAuthAccessTokenRedisEntity;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthAccessTokenRedisRepository;
import allcount.poc.user.entity.AllcountUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for determining the Access Token to use.
 */
@Service
public class OpenBankingOAuthAccessTokenDetermineService {

    private final transient OpenBankingOAuthAccessTokenRedisRepository openBankingOAuthAccessTokenRedisRepository;
    private final transient OpenBankingOAuthAccessTokenRefreshService openBankingOAuthAccessTokenRefreshService;

    /**
     * Constructor.
     *
     * @param openBankingOAuthAccessTokenRedisRepository - the OpenBankingOAuthAccessTokenRedisRepository
     * @param openBankingOAuthAccessTokenRefreshService - the OpenBankingOAuthAccessTokenRefreshService
     */
    @Autowired
    public OpenBankingOAuthAccessTokenDetermineService(
            OpenBankingOAuthAccessTokenRedisRepository openBankingOAuthAccessTokenRedisRepository,
            OpenBankingOAuthAccessTokenRefreshService openBankingOAuthAccessTokenRefreshService) {
        this.openBankingOAuthAccessTokenRedisRepository = openBankingOAuthAccessTokenRedisRepository;
        this.openBankingOAuthAccessTokenRefreshService = openBankingOAuthAccessTokenRefreshService;
    }

    /**
     * Determines the Access Token to use.
     *
     * @param user - the user
     * @param bank - the bank
     * @return the OpenBankingOAuthAccessTokenRedisEntity
     * @throws JsonProcessingException - if the response cannot be processed
     */
    public OpenBankingOAuthAccessTokenRedisEntity determineAccessToken(AllcountUser user, OpenBankingBankEnum bank)
            throws JsonProcessingException {
        Optional<OpenBankingOAuthAccessTokenRedisEntity> accessToken = openBankingOAuthAccessTokenRedisRepository
                .findById(OpenBankingOAuthAccessTokenRedisEntity.generateRedisId(bank, user.getId()));

        if (accessToken.isPresent()) {
            return accessToken.get();
        } else {
            return openBankingOAuthAccessTokenRefreshService.refreshAccessToken(user, bank);
        }
    }
}
