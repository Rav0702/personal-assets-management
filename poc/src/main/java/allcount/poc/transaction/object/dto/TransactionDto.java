package allcount.poc.transaction.object.dto;

import allcount.poc.core.domain.object.dto.AllcountDto;
import allcount.poc.shared.annotation.ValidCurrencyCode;
import allcount.poc.shared.annotation.ValidIbanCode;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * DTO for the Transaction.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto extends AllcountDto {

    @NonNull
    private BigDecimal amount;

    @ValidCurrencyCode
    @Nullable
    private String currencyCode;

    @NonNull
    private LocalDate bookingDate;

    @Nullable
    private LocalDate valueDate;

    @ValidIbanCode
    @NonNull
    private String originIban;

    @Nullable
    private String counterPartyName;

    @ValidIbanCode
    @Nullable
    private String counterPartyIban;

    @Nullable
    private String originBankId;
}
