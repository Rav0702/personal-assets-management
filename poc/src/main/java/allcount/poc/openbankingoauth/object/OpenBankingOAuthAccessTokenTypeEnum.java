package allcount.poc.openbankingoauth.object;

public enum OpenBankingOAuthAccessTokenTypeEnum {
    BEARER("Bearer");

    public static final String ERROR_UNKNOWN_ACCESS_TOKEN_TYPE = "Unknown token type: ";
    private final String openBankingOAuthAccessTokenType;

    OpenBankingOAuthAccessTokenTypeEnum(String openBankingOAuthAccessTokenType) {
        this.openBankingOAuthAccessTokenType = openBankingOAuthAccessTokenType;
    }

    /**
     * Returns the OpenBankingOAuthAccessTokenType.
     *
     * @return the OpenBankingOAuthAccessTokenType
     */
    @Override
    public String toString() {
        return openBankingOAuthAccessTokenType;
    }

    /**
     * Returns the OpenBankingOAuthAccessTokenTypeEnum from the given string.
     *
     * @param openBankingOAuthAccessTokenType - the string
     * @return the OpenBankingOAuthAccessTokenTypeEnum
     */
    public static OpenBankingOAuthAccessTokenTypeEnum fromValue(String openBankingOAuthAccessTokenType) {
        for (OpenBankingOAuthAccessTokenTypeEnum tokenType : OpenBankingOAuthAccessTokenTypeEnum.values()) {
            if (tokenType.openBankingOAuthAccessTokenType.equalsIgnoreCase(openBankingOAuthAccessTokenType)) {
                return tokenType;
            }
        }

        throw new IllegalArgumentException(ERROR_UNKNOWN_ACCESS_TOKEN_TYPE + openBankingOAuthAccessTokenType);
    }
}
