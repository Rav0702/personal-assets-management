package allcount.poc.transaction.mapper.responsetodto.list;

import allcount.poc.transaction.object.dto.TransactionDto;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * Mapper for converting Open Banking list transactions response to a DTO list.
 */
public interface OpenBankingListTransactionResponseToDtoMapper {

    /**
     * Maps the response to a list of DTOs.
     *
     * @param response the response.
     * @return list of mapped DTOs.
     */
    List<TransactionDto> mapToDto(JsonNode response);
}
