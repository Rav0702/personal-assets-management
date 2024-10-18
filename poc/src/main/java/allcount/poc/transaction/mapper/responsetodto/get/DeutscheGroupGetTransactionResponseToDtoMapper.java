package allcount.poc.transaction.mapper.responsetodto.get;

import allcount.poc.transaction.object.dto.TransactionDto;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting Deutsche Group transaction response to a DTO.
 *
 * <p>
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
        throw new RuntimeException("Not implemented");
    }
}
