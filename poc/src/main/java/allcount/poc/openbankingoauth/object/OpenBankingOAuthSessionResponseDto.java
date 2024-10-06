package allcount.poc.openbankingoauth.object;

import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for the OpenBankingOAuthSession.
 */
@Setter
@Getter
@NoArgsConstructor
public class OpenBankingOAuthSessionResponseDto implements Serializable {
    private UUID id;
    private OpenBankingBankEnum bank;
    private OpenBankingOAuthSessionStatusEnum status;
    private String redirectLoginUri;
    private String codeVerifier;
    private UUID state;
    private UUID userId;
}
