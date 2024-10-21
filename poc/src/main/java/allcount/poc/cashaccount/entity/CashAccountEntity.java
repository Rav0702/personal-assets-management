package allcount.poc.cashaccount.entity;

import allcount.poc.core.domain.entity.OpenBankingEntity;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.shared.annotation.ValidISO4217Alpha3Code;
import allcount.poc.shared.annotation.ValidIbanCode;
import allcount.poc.user.entity.AllcountUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * Entity for the Account.
 */
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cash_account")
public class CashAccountEntity extends OpenBankingEntity {
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
    @ValidIbanCode
    private String iban;

    @Column(name = "product_description")
    private String productDescription;

    /**
     * Constructor.
     *
     * @param bank              the bank
     * @param user              the user
     * @param accountType       the account type
     * @param bic               the BIC
     * @param currencyCode      the currency code
     * @param currentBalance    the current balance
     * @param iban              the IBAN
     * @param productDescription the product description
     */
    @Builder
    public CashAccountEntity(OpenBankingBankEnum bank,
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
