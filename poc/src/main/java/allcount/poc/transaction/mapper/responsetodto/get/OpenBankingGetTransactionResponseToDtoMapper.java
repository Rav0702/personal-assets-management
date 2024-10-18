package allcount.poc.transaction.mapper.responsetodto.get;

import allcount.poc.transaction.object.dto.TransactionDto;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Mapper for converting Open Banking transaction response to a DTO.
 */
public interface OpenBankingGetTransactionResponseToDtoMapper {

    /**
     * Maps the response to the DTO.
     *
     * @param response json body of the response
     * @return the DTO
     */
    TransactionDto mapToDto(JsonNode response);
}
