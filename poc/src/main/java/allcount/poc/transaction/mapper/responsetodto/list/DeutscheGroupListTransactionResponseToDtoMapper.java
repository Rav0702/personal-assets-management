package allcount.poc.transaction.mapper.responsetodto.list;

import allcount.poc.transaction.mapper.responsetodto.get.DeutscheGroupGetTransactionResponseToDtoMapper;
import allcount.poc.transaction.object.dto.TransactionListDto;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Mapper for converting Deutsche Group list transactions response to a DTO.
 *
 * <p>
 * Works with:
 * <ul>
 * <li>Deutsche Bank</li>
 * <li>Postbank</li>
 * </ul>
 */
@Component
public class DeutscheGroupListTransactionResponseToDtoMapper implements OpenBankingListTransactionResponseToDtoMapper {

    private final transient DeutscheGroupGetTransactionResponseToDtoMapper deutscheGroupGetTransactionResponseToDtoMapper;

    /**
     * Constructor.
     */
    @Autowired
    public DeutscheGroupListTransactionResponseToDtoMapper(DeutscheGroupGetTransactionResponseToDtoMapper deutscheGroupGetTransactionResponseToDtoMapper) {
        this.deutscheGroupGetTransactionResponseToDtoMapper = deutscheGroupGetTransactionResponseToDtoMapper;
    }


    /**
     * Maps the response to the DTO.
     *
     * @param response the response
     * @return the DTO
     */
    @Override
    public TransactionListDto mapToDto(JsonNode response) {
        JsonNode transactions = response.get("transactions");
        assert transactions != null;
        assert transactions.isArray();

        TransactionListDto transactionListDto = TransactionListDto
                .builder()
                .transactions(new ArrayList<>(transactions.size()))
                .build();

        for (JsonNode transaction : transactions) {
            transactionListDto.getTransactions().addLast(deutscheGroupGetTransactionResponseToDtoMapper.mapToDto(transaction));
        }

        return null;
    }
}
