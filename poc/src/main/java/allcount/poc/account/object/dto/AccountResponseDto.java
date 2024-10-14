package allcount.poc.account.object.dto;

import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.object.enums.OpenBankingOAuthAccessTokenTypeEnum;
import allcount.poc.user.entity.AllcountUser;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO for the AccountResponse.
 */
@Setter
@Getter
@NoArgsConstructor
public class AccountResponseDto {
    private OpenBankingBankEnum bank;
    private String accountType;
    private String bic;
    private String currencyCode;
    private BigDecimal currentBalance;
    private String iban;
    private String productDescription;

    @Builder(toBuilder = true)
    public AccountResponseDto(
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
