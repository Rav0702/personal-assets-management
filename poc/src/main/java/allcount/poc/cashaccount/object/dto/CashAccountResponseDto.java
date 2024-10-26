package allcount.poc.cashaccount.object.dto;

import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for the AccountResponse.
 */
@Setter
@Getter
@NoArgsConstructor
public class CashAccountResponseDto {
    private OpenBankingBankEnum bank;
    private String accountType;
    private String bic;
    private String currencyCode;
    private BigDecimal currentBalance;
    private String iban;
    private String productDescription;

    /**
     * Constructor.
     *
     * @param bank               the bank
     * @param accountType        the account type
     * @param bic                the BIC
     * @param currencyCode       the currency code
     * @param currentBalance     the current balance
     * @param iban               the IBAN
     * @param productDescription the product description
     */
    @Builder(toBuilder = true)
    public CashAccountResponseDto(
            OpenBankingBankEnum bank,
            String accountType,
            String bic,
            String currencyCode,
            BigDecimal currentBalance,
            String iban,
            String productDescription
    ) {
        this.bank = bank;
        this.accountType = accountType;
        this.bic = bic;
        this.currencyCode = currencyCode;
        this.currentBalance = currentBalance;
        this.iban = iban;
        this.productDescription = productDescription;
    }
}
