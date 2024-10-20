package allcount.poc.transaction.mapper.responsetodto.list;

import allcount.poc.transaction.object.dto.TransactionListDto;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Mapper for converting Open Banking list transactions response to a DTO.
 */
public interface OpenBankingListTransactionResponseToDtoMapper {

    /**
     * Maps the response to the DTO.
     *
     * @param response json body of the response.
     * @return the DTO.
     */
    TransactionListDto mapToDto(JsonNode response);
}
