package allcount.poc.transaction.mapper.responsetodto.get;

import allcount.poc.transaction.object.dto.TransactionDto;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting Deutsche Group transaction response to a DTO.
 *
 * <br>
 * Works with:
 * <ul>
 * <li>Deutsche Bank</li>
 * <li>Postbank</li>
 * </ul>
 */
@Component
public class DeutscheGroupGetTransactionResponseToDtoMapper implements OpenBankingGetTransactionResponseToDtoMapper {
    @Override
    public TransactionDto mapToDto(JsonNode response) {
        TransactionDto dto = TransactionDto.builder()
                .amount(response.get("amount").decimalValue())
                .bookingDate(LocalDate.parse(response.get("bookingDate").textValue()))
                .originIban(response.get("originIban").textValue())
                .build();

        if (response.has("currencyCode")) {
            dto.setCurrencyCode(response.get("currencyCode").textValue());
        }

        if (response.has("valueDate")) {
            dto.setValueDate(LocalDate.parse(response.get("valueDate").textValue()));
        }

        if (response.has("counterPartyName")) {
            dto.setCounterPartyName(response.get("counterPartyName").textValue());
        }

        if (response.has("counterPartyIban")) {
            dto.setCounterPartyIban(response.get("counterPartyIban").textValue());
        }

        if (response.has("id")) {
            dto.setOriginBankId(response.get("id").textValue());
        }

        return dto;
    }
}