package allcount.poc.openbankingoauth.entity;

import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.object.enums.OpenBankingOAuthAccessTokenTypeEnum;
import allcount.poc.user.entity.AllcountUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity storing the access token retrieved from OpenBanking OAuth.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "open_banking_refresh_token")
public class OpenBankingOAuthRefreshTokenEntity extends OpenBankingOAuthEntity {
    @Column(nullable = false, columnDefinition = "TEXT")
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OpenBankingOAuthAccessTokenTypeEnum tokenType;

    @Column(nullable = false)
    private String scope;

    /**
     * Constructor.
     *
     * @param bank         - the bank
     * @param user         - the user
     * @param refreshToken - the refresh token
     * @param tokenType    - the token type
     * @param scope        - the scope
     */
    @Builder
    public OpenBankingOAuthRefreshTokenEntity(
            OpenBankingBankEnum bank,
            AllcountUser user,
            String refreshToken,
            OpenBankingOAuthAccessTokenTypeEnum tokenType,
            String scope
    ) {
        super(bank, user);
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.scope = scope;
    }

    @Override
    protected String toStringForHashOnly() {
        return "";
    }
}
