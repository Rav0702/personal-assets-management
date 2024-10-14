package allcount.poc.openbankingoauth.entity;

import allcount.poc.core.domain.entity.AllcountRedisEntity;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.object.enums.OpenBankingOAuthAccessTokenTypeEnum;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

/**
 * Entity storing the access token retrieved from OpenBanking OAuth with expiration on Redis.
 */
@Setter
@Getter
@RedisHash("OpenBankingOAuthAccessToken")
public class OpenBankingOAuthAccessTokenRedisEntity extends AllcountRedisEntity {

    private String accessToken;
    private OpenBankingOAuthAccessTokenTypeEnum tokenType;
    private UUID userId;
    private OpenBankingBankEnum bank;
    @TimeToLive
    private Long expiresIn;

    /**
     * Constructor.
     *
     * @param accessToken - the access token
     * @param tokenType   - the token type
     * @param userId      - the user id
     * @param bank        - the bank
     * @param expiresIn   - the expiration time
     */
    public OpenBankingOAuthAccessTokenRedisEntity(
            String accessToken,
            OpenBankingOAuthAccessTokenTypeEnum tokenType,
            UUID userId,
            OpenBankingBankEnum bank,
            Long expiresIn
    ) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.userId = userId;
        this.bank = bank;
        this.expiresIn = expiresIn;
        this.id = generateRedisId();
    }

    /**
     * Generates the Redis ID.
     *
     * @param bank   - the bank
     * @param userId - the user id
     * @return the Redis ID
     */
    public static String generateRedisId(OpenBankingBankEnum bank, UUID userId) {
        return bank + REDIS_KEY_SEPARATOR + userId;
    }

    /**
     * Generates the Redis ID.
     *
     * @return the Redis ID
     */
    protected String generateRedisId() {
        return this.bank + REDIS_KEY_SEPARATOR + this.userId;
    }
}
