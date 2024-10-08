package allcount.poc.openbankingoauth.object.dto;

import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.object.enums.OpenBankingOAuthSessionStatusEnum;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for the OpenBankingOAuthSession.
 */
@Setter
@Getter
@NoArgsConstructor
public class OpenBankingOAuthSessionResponseDto extends OpenBankingOAuthDto {
    private OpenBankingOAuthSessionStatusEnum status;
    private String redirectLoginUri;
    private String codeVerifier;
    private UUID state;

    /**
     * Constructor.
     *
     * @param id - the id
     * @param userId - the userId
     * @param bank - the bank
     * @param status - the status
     * @param redirectLoginUri - the redirectLoginUri
     * @param codeVerifier - the codeVerifier
     * @param state - the state
     */
    @Builder(toBuilder = true)
    public OpenBankingOAuthSessionResponseDto(
            UUID id,
            UUID userId,
            OpenBankingBankEnum bank,
            OpenBankingOAuthSessionStatusEnum status,
            String redirectLoginUri,
            String codeVerifier,
            UUID state
    ) {
        super(id, userId, bank);
        this.status = status;
        this.redirectLoginUri = redirectLoginUri;
        this.codeVerifier = codeVerifier;
        this.state = state;
    }
}
