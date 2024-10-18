package allcount.poc.account.entity;

import allcount.poc.openbankingoauth.entity.OpenBankingOAuthEntity;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.shared.annotation.ValidIBANCode;
import allcount.poc.shared.annotation.ValidISO4217Alpha3Code;
import allcount.poc.user.entity.AllcountUser;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;


/**
 * Entity for the Account.
 */
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
public class AccountEntity extends OpenBankingOAuthEntity {
    @Column(name = "account_type")
    private String accountType;

    @Column(name = "bic")
    private String bic;

    @Column(name = "currency", length = 3, nullable = false)
    @ValidISO4217Alpha3Code
    private String currencyCode;

    @Column(name = "current_balance", precision = 15, scale = 2, nullable = false)
    @NonNull
    private BigDecimal currentBalance;

    @Column(name = "iban", nullable = false)
    @ValidIBANCode
    private String iban;

    @Column(name = "product_description")
    private String productDescription;

    @Builder
    public AccountEntity(OpenBankingBankEnum bank,
                         AllcountUser user,
                         String accountType,
                         String bic,
                         String currencyCode,
                         BigDecimal currentBalance,
                         String iban,
                         String productDescription) {
        super(bank, user);
        this.accountType = accountType;
        this.bic = bic;
        this.currencyCode = currencyCode;
        this.currentBalance = currentBalance;
        this.iban = iban;
        this.productDescription = productDescription;
    }

    /**
     * String representation of the object for hashing only.
     *
     * @return String representation of the object for hashing only.
     */
    @Override
    protected String toStringForHashOnly() {
        return this.bic + this.currencyCode + this.iban;
    }
}
