package allcount.poc.transaction.object.dto;

import allcount.poc.core.domain.object.dto.AllcountDto;
import allcount.poc.shared.annotation.ValidIBANCode;
import allcount.poc.shared.annotation.ValidISO4217Alpha3Code;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.*;
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

    @ValidISO4217Alpha3Code
    @Nullable
    private String currencyCode;

    @NonNull
    private LocalDate bookingDate;

    @Nullable
    private LocalDate valueDate;

    @ValidIBANCode
    @NonNull
    private String originIban;

    @Nullable
    private String counterPartyName;

    //todo: add validation
    @Nullable
    private String counterPartyIban;

    @Nullable
    private String originBankId;
}
