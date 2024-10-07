package allcount.poc.openbankingoauth.entity;

import allcount.poc.openbankingoauth.object.OpenBankingBankEnum;
import allcount.poc.user.entity.AllcountUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "open_banking_access_token")
public class OpenBankingOAuthAccessTokenEntity extends OpenBankingOAuthEntity {

    @Column(nullable = false, columnDefinition = "TEXT")
    private String accessToken;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String refreshToken;

    @Column(nullable = false)
    private String tokenType;

    @Column(nullable = false)
    private Long expiresIn;

    @Column(nullable = false)
    private String scope;

    /**
     * Constructor.
     *
     * @param bank         - the bank
     * @param user         - the user
     * @param accessToken  - the access token
     * @param refreshToken - the refresh token
     * @param tokenType    - the token type
     * @param expiresIn    - the expiration time
     * @param scope        - the scope
     */
    @Builder
    public OpenBankingOAuthAccessTokenEntity(
            OpenBankingBankEnum bank,
            AllcountUser user,
            String accessToken,
            String refreshToken,
            String tokenType,
            Long expiresIn,
            String scope
    ) {
        super(bank, user);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.scope = scope;
    }

    @Override
    protected String toStringForHashOnly() {
        return "";
    }
}
