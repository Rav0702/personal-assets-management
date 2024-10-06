package allcount.poc.open_banking_authorization.object;

import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
