package allcount.poc.openbankingoauth.mapper;

import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Mapper for the Open Banking banks to their token refresh path URIs.
 */
@Component
public class OpenBankingBankToRefreshTokenPathUriMapper {

    private static final Map<OpenBankingBankEnum, String> BANK_TO_TOKEN_REFRESH_PATH_URI = Map.of(
            OpenBankingBankEnum.DEUTSCHE_BANK, "/gw/oidc/token",
            OpenBankingBankEnum.NORIS_BANK, "/gw/oidc/token"
    );

    /**
     * Returns the token refresh path URI for the given bank.
     *
     * @param bank - the bank
     * @return the token refresh path URI for the given bank
     */
    public String getTokenRefreshPathUri(OpenBankingBankEnum bank) {
        return BANK_TO_TOKEN_REFRESH_PATH_URI.get(bank);
    }
}
