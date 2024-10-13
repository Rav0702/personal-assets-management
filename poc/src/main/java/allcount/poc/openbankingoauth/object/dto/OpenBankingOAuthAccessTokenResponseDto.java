package allcount.poc.openbankingoauth.object.dto;

import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.object.enums.OpenBankingOAuthAccessTokenTypeEnum;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for the OpenBankingOAuthAccessTokenResponse.
 */
@Setter
@Getter
@NoArgsConstructor
public class OpenBankingOAuthAccessTokenResponseDto extends OpenBankingOAuthDto {
    private String refreshToken;
    private OpenBankingOAuthAccessTokenTypeEnum tokenType;
    private String scope;

    /**
     * Constructor.
     *
     * @param id           - the id
     * @param userId       - the userId
     * @param bank         - the bank
     * @param refreshToken - the refreshToken
     * @param tokenType    - the tokenType
     * @param scope        - the scope
     */
    @Builder(toBuilder = true)
    public OpenBankingOAuthAccessTokenResponseDto(
            UUID id,
            UUID userId,
            OpenBankingBankEnum bank,
            String refreshToken,
            OpenBankingOAuthAccessTokenTypeEnum tokenType,
            String scope
    ) {
        super(id, userId, bank);
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.scope = scope;
    }
}
