package allcount.poc.open_banking_authorization.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "open_banking_access_token")
public class OpenBankingOAuthAccessTokenEntity extends OpenBankingOAuthEntity {

    @Column(nullable=false)
    private String accessToken;

    @Column(nullable=false)
    private String refreshToken;

    @Column(nullable=false)
    private String tokenType;

    @Column(nullable=false)
    private Long expiresIn;

    @Column(nullable=false)
    private String scope;

    @Override
    protected String toStringForHashOnly() {
        return "";
    }
}
