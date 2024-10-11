package allcount.poc.openbankingoauth.object.dto;

import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.object.enums.OpenBankingOAuthAccessTokenTypeEnum;

import java.time.LocalDateTime;
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
    private String accessToken;
    private String refreshToken;
    private OpenBankingOAuthAccessTokenTypeEnum tokenType;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String scope;

    /**
     * Constructor.
     *
     * @param id            - the id
     * @param userId        - the userId
     * @param bank          - the bank
     * @param accessToken   - the accessToken
     * @param refreshToken  - the refreshToken
     * @param tokenType     - the tokenType
     * @param startDateTime - the start date of the token
     * @param endDateTime   - the end date of the token
     * @param scope         - the scope
     */
    @Builder(toBuilder = true)
    public OpenBankingOAuthAccessTokenResponseDto(
            UUID id,
            UUID userId,
            OpenBankingBankEnum bank,
            String accessToken,
            String refreshToken,
            OpenBankingOAuthAccessTokenTypeEnum tokenType,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            String scope
    ) {
        super(id, userId, bank);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.scope = scope;
    }
}
