package allcount.poc.transaction.provider;

import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.shared.mapper.JsonResponseToDtoMapper;
import allcount.poc.transaction.mapper.responsetodto.list.DeutscheGroupListTransactionResponseToDtoMapper;
import allcount.poc.transaction.object.dto.TransactionDto;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Provider for transaction response to DTO mappers.
 */
@Component
public class OpenBankingListTransactionResponseToDtoMapperProvider {

    private final Map<OpenBankingBankEnum, JsonResponseToDtoMapper<List<TransactionDto>>> bankToMapper;

    /**
     * Constructor.
     */
    @Autowired
    public OpenBankingListTransactionResponseToDtoMapperProvider(
            DeutscheGroupListTransactionResponseToDtoMapper deutscheGroupListTransactionResponseToDtoMapper
    ) {
        this.bankToMapper = Map.of(
            OpenBankingBankEnum.DEUTSCHE_BANK, deutscheGroupListTransactionResponseToDtoMapper,
            OpenBankingBankEnum.NORIS_BANK, deutscheGroupListTransactionResponseToDtoMapper
        );
    }

    /**
     * Returns true if we have a mapper for the given bank.
     *
     * @param bank enum representing the bank.
     * @return true if we have a mapper for the given bank.
     */
    public boolean doesBankSupportTransactionRetrieval(OpenBankingBankEnum bank) {
        return bankToMapper.containsKey(bank);
    }

    /**
     * Returns transaction response to DTO mapper for the given bank.
     *
     * @param bank enum representing the bank.
     * @return transaction response to DTO mapper for the given bank.
     */
    public JsonResponseToDtoMapper<List<TransactionDto>> getMapper(OpenBankingBankEnum bank) {
        return bankToMapper.get(bank);
    }

}
