package allcount.poc.open_banking_authorization.object;

import java.util.UUID;
import lombok.Setter;

@Setter
public class OpenBankingOAuthAccessTokenResponseDto {

    private UUID id;
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private String scope;
    private UUID userId;
    private OpenBankingBankEnum bank;
}
