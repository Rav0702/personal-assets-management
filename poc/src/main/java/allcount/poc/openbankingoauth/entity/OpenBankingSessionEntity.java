package allcount.poc.openbankingoauth.entity;

import allcount.poc.core.domain.entity.OpenBankingEntity;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.object.enums.OpenBankingOAuthSessionStatusEnum;
import allcount.poc.user.entity.AllcountUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity for the OpenBankingAuthorization.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "open_banking_oauth_session")
public class OpenBankingSessionEntity extends OpenBankingEntity {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OpenBankingOAuthSessionStatusEnum status;

    @Column(nullable = false, updatable = false)
    private String codeVerifier;

    @Column(nullable = false, updatable = false)
    private UUID state;

    @Column(nullable = false, columnDefinition = "TEXT", updatable = false)
    private String redirectLoginUri;

    /**
     * Constructor for the OpenBankingSessionEntity.
     *
     * @param bank             the bank
     * @param user             the user
     * @param status           the status
     * @param codeVerifier     the code verifier
     * @param state            the state
     * @param redirectLoginUri the redirect login uri
     */
    @Builder
    public OpenBankingSessionEntity(
            OpenBankingBankEnum bank,
            AllcountUser user,
            OpenBankingOAuthSessionStatusEnum status,
            String codeVerifier,
            UUID state,
            String redirectLoginUri
    ) {
        super(bank, user);
        this.status = status;
        this.codeVerifier = codeVerifier;
        this.state = state;
        this.redirectLoginUri = redirectLoginUri;
    }

    @Override
    protected String toStringForHashOnly() {
        return "";
    }
}
