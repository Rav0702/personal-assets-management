package allcount.poc.open_banking_authorization.entity;

import allcount.poc.open_banking_authorization.object.OpenBankingOAuthSessionStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity for the OpenBankingAuthorization.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "open_banking_oauth_session")
public class OpenBankingOAuthSessionEntity extends OpenBankingOAuthEntity {
    @Enumerated(EnumType.STRING)
    @Column(nullable=false, updatable = false)
    private OpenBankingOAuthSessionStatusEnum status;

    @Column(nullable=false, updatable = false)
    private String codeVerifier;

    @Column(nullable=false)
    private UUID state;

    @Column(nullable=false, columnDefinition = "TEXT", updatable = false)
    private String redirectLoginUri;


    @Override
    protected String toStringForHashOnly() {
        return "";
    }
}
