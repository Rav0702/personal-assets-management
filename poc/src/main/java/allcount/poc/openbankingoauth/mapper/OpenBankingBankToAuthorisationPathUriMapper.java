package allcount.poc.openbankingoauth.mapper;

import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Mapper for the Open Banking banks to their authorisation path URIs.
 */
@Component
public class OpenBankingBankToAuthorisationPathUriMapper {

    private static final Map<OpenBankingBankEnum, String> BANK_TO_AUTHORISATION_PATH_URI = Map.of(
            OpenBankingBankEnum.DEUTSCHE_BANK, "/gw/oidc/authorize",
            OpenBankingBankEnum.NORIS_BANK, "/gw/oidc/nb/authorize"
    );

    /**
     * Returns the authorisation path URI for the given bank.
     *
     * @param bank - the bank
     * @return the authorisation path URI for the given bank
     */
    public String getAuthorisationPathUri(OpenBankingBankEnum bank) {
        return BANK_TO_AUTHORISATION_PATH_URI.get(bank);
    }
}
