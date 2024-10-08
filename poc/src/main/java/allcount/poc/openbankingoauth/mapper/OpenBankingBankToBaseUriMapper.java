package allcount.poc.openbankingoauth.mapper;

import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Mapper for the Open Banking banks to their base URIs.
 */
@Component
public class OpenBankingBankToBaseUriMapper {
    /**
     * Map from the Open Banking banks to their base URIs.
     */
    private static final Map<OpenBankingBankEnum, String> BANK_TO_BASE_URI = Map.of(
            OpenBankingBankEnum.DEUTSCHE_BANK, "https://simulator-api.db.com",
            OpenBankingBankEnum.NORIS_BANK, " https://simulator-api.norisbank.de",
            OpenBankingBankEnum.POST_BANK, "https://simulator-api.postbank.de"
    );

    /**
     * Returns the base URI for the given bank.
     *
     * @param bank - the bank
     * @return the base URI for the given bank
     */
    public String getBaseUri(OpenBankingBankEnum bank) {
        return BANK_TO_BASE_URI.get(bank);
    }
}
