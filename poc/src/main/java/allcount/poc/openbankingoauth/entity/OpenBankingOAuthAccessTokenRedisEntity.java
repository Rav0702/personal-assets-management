package allcount.poc.openbankingoauth.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Setter
@Getter
@RedisHash("OpenBankingOAuthAccessToken")
public class OpenBankingOAuthAccessTokenRedisEntity implements Serializable {
    private String id;
    private String accessToken;
    @TimeToLive
    private Long expiresIn;
}
