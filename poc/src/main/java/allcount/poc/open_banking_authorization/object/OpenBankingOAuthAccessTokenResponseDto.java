package allcount.poc.open_banking_authorization.object;

import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OpenBankingOAuthAccessTokenResponseDto implements Serializable {

    private UUID id;
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private String scope;
    private UUID userId;
    private OpenBankingBankEnum bank;
}
