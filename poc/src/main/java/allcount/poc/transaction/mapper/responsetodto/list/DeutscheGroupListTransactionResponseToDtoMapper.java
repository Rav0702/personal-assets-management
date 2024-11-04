package allcount.poc.transaction.mapper.responsetodto.list;

import allcount.poc.shared.mapper.JsonResponseToDtoMapper;
import allcount.poc.transaction.mapper.responsetodto.get.DeutscheGroupGetTransactionResponseToDtoMapper;
import allcount.poc.transaction.object.dto.TransactionDto;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Mapper for converting Deutsche Group list transactions response to a DTO list.
 *
 * <br>
 * Works with:
 * <ul>
 * <li>Deutsche Bank</li>
 * <li>Postbank</li>
 * </ul>
 */
@Component
public class DeutscheGroupListTransactionResponseToDtoMapper implements JsonResponseToDtoMapper<List<TransactionDto>> {

    private final transient DeutscheGroupGetTransactionResponseToDtoMapper
            deutscheGroupGetTransactionResponseToDtoMapper;

    /**
     * Constructor.
     */
    @Autowired
    public DeutscheGroupListTransactionResponseToDtoMapper(
            DeutscheGroupGetTransactionResponseToDtoMapper deutscheGroupGetTransactionResponseToDtoMapper) {
        this.deutscheGroupGetTransactionResponseToDtoMapper = deutscheGroupGetTransactionResponseToDtoMapper;
    }


    /**
     * Maps the response to a list of DTOs.
     *
     * @param response the response.
     * @return list of mapped DTOs.
     */
    @Override
    public List<TransactionDto> mapToDto(JsonNode response) {
        JsonNode transactions = response.get("transactions");
        assert transactions != null;
        assert transactions.isArray();

        List<TransactionDto> transactionDtoList = new ArrayList<>();

        for (JsonNode transaction : transactions) {
            transactionDtoList.add(deutscheGroupGetTransactionResponseToDtoMapper.mapToDto(transaction));
        }

        return transactionDtoList;
    }
}
