package allcount.poc.open_banking_authorization.object;

import java.util.Map;

public enum OpenBankingBankEnum {
    DEUTSCHE_BANK,
    NORIS_BANK,
    POST_BANK;

    private static final Map<OpenBankingBankEnum, String> BANK_TO_BASE_URI = Map.of(
        DEUTSCHE_BANK, "https://simulator-api.db.com",
        NORIS_BANK, " https://simulator-api.norisbank.de",
        POST_BANK, "https://simulator-api.postbank.de"
    );

    public String getBaseUri() {
        return BANK_TO_BASE_URI.get(this);
    }
}
