package allcount.poc.openbankingoauth.object;

import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for the OpenBankingOAuthAccessTokenResponse.
 */
@Setter
@Getter
@NoArgsConstructor
public class OpenBankingOAuthAccessTokenResponseDto implements Serializable {

    private UUID id;
    private String accessToken;
    private String refreshToken;
    private OpenBankingOAuthAccessTokenTypeEnum tokenType;
    private Long expiresIn;
    private String scope;
    private UUID userId;
    private OpenBankingBankEnum bank;
}
