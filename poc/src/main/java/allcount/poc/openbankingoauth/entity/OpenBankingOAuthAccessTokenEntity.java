package allcount.poc.openbankingoauth.entity;

import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.object.enums.OpenBankingOAuthAccessTokenTypeEnum;
import allcount.poc.user.entity.AllcountUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OpenBankingOAuthAccessTokenTypeEnum tokenType;

    @Column(nullable = false)
    private Long expiresIn;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @Column(nullable = false)
    private String scope;

    /**
     * Constructor.
     *
     * @param bank          - the bank
     * @param user          - the user
     * @param accessToken   - the access token
     * @param refreshToken  - the refresh token
     * @param tokenType     - the token type
     * @param startDateTime - the start date of the token
     *                      (the end date is calculated based on the token type)
     * @param scope         - the scope
     */
    @Builder
    public OpenBankingOAuthAccessTokenEntity(
            OpenBankingBankEnum bank,
            AllcountUser user,
            String accessToken,
            String refreshToken,
            OpenBankingOAuthAccessTokenTypeEnum tokenType,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            String scope,
            Long expiresIn
    ) {
        super(bank, user);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.scope = scope;
        this.expiresIn = expiresIn;
    }

    @Override
    protected String toStringForHashOnly() {
        return "";
    }
}
