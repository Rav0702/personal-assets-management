package allcount.poc.transaction.provider;

import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.shared.mapper.JsonResponseToDtoMapper;
import allcount.poc.transaction.mapper.responsetodto.get.DeutscheGroupGetTransactionResponseToDtoMapper;
import allcount.poc.transaction.object.dto.TransactionDto;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Provider for transaction response to DTO mappers.
 */
@Component
public class OpenBankingGetTransactionResponseToDtoMapperProvider {
    private final Map<OpenBankingBankEnum, JsonResponseToDtoMapper<TransactionDto>> mappingBankToTransactionResponseToDtoMapper;

    /**
     * Constructor.
     */
    @Autowired
    public OpenBankingGetTransactionResponseToDtoMapperProvider(
            DeutscheGroupGetTransactionResponseToDtoMapper deutscheGroupGetTransactionResponseToDtoMapper
    ) {
        this.mappingBankToTransactionResponseToDtoMapper = Map.of(
            OpenBankingBankEnum.DEUTSCHE_BANK, deutscheGroupGetTransactionResponseToDtoMapper,
            OpenBankingBankEnum.NORIS_BANK, deutscheGroupGetTransactionResponseToDtoMapper
        );
    }

    /**
     * Returns true if we have a mapper for the given bank.
     *
     * @param bank enum representing the bank.
     * @return true if we have a mapper for the given bank.
     */
    public boolean doesBankSupportTransactionRetrieval(OpenBankingBankEnum bank) {
        return mappingBankToTransactionResponseToDtoMapper.containsKey(bank);
    }

    /**
     * Returns transaction response to DTO mapper for the given bank.
     *
     * @param bank enum representing the bank.
     * @return transaction response to DTO mapper for the given bank.
     */
    public JsonResponseToDtoMapper<TransactionDto> getMapper(OpenBankingBankEnum bank) {
        return mappingBankToTransactionResponseToDtoMapper.get(bank);
    }

}
