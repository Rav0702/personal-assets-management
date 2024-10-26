package allcount.poc.transaction.mapper;

import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import java.util.Map;
import org.springframework.stereotype.Component;


/**
 * Mapper for the Open Banking banks to transaction-related URIs.
 */
@Component
public class OpenBankingBankToTransactionUriMapper {
    /**
     * Map from the Open Banking banks to uris for listing transactions.
     */
    private static final Map<OpenBankingBankEnum, String> BANK_TO_LIST_TRANSACTIONS_URI = Map.of(
            OpenBankingBankEnum.DEUTSCHE_BANK, "/gw/dbapi/banking/transactions/v2",
            OpenBankingBankEnum.NORIS_BANK, "/gw/dbapi/banking/transactions/v2"
    //        OpenBankingBankEnum.POST_BANK, "todo POSTBANK DOES NOT SUPPORT LISTING TRANSACTIONS"
    );

    /**
     * Returns true if the given bank supports transaction retrieval. (i.e. we have a URI for listing transactions).
     *
     * @param bank the bank.
     * @return true if the given bank supports transaction retrieval.
     */
    public boolean doesBankSupportTransactionRetrieval(OpenBankingBankEnum bank) {
        return BANK_TO_LIST_TRANSACTIONS_URI.containsKey(bank);
    }

    /**
     * Returns uri for listing transactions for the given bank.
     * <i>complete uri = baseUri + uri.</i>
     *
     * @param bank the bank.
     * @return the base URI for the given bank.
     */
    public String getListTransactionsUri(OpenBankingBankEnum bank) {
        return BANK_TO_LIST_TRANSACTIONS_URI.get(bank);
    }
}
