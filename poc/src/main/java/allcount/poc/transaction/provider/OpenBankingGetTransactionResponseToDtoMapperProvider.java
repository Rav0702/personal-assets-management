package allcount.poc.transaction.provider;

import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.transaction.mapper.responsetodto.get.DeutscheGroupGetTransactionResponseToDtoMapper;
import allcount.poc.transaction.mapper.responsetodto.get.OpenBankingGetTransactionResponseToDtoMapper;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Provider for transaction response to DTO mappers.
 */
@Component
public class OpenBankingGetTransactionResponseToDtoMapperProvider {
    private final Map<OpenBankingBankEnum, OpenBankingGetTransactionResponseToDtoMapper> bankToMapper;

    /**
     * Constructor.
     */
    @Autowired
    public OpenBankingGetTransactionResponseToDtoMapperProvider(
            DeutscheGroupGetTransactionResponseToDtoMapper deutscheGroupGetTransactionResponseToDtoMapper
    ) {
        this.bankToMapper = Map.of(
            OpenBankingBankEnum.DEUTSCHE_BANK, deutscheGroupGetTransactionResponseToDtoMapper,
            OpenBankingBankEnum.NORIS_BANK, deutscheGroupGetTransactionResponseToDtoMapper
        );
    }

    /**
     * Returns true if we have a mapper for the given bank.
     *
     * @param bank enum representing the bank
     * @return true if we have a mapper for the given bank
     */
    public boolean doesBankSupportTransactionRetrieval(OpenBankingBankEnum bank) {
        return bankToMapper.containsKey(bank);
    }

    /**
     * Returns transaction response to DTO mapper for the given bank.
     *
     * @param bank enum representing the bank
     * @return transaction response to DTO mapper for the given bank
     */
    public OpenBankingGetTransactionResponseToDtoMapper getMapper(OpenBankingBankEnum bank) {
        return bankToMapper.get(bank);
    }

}
