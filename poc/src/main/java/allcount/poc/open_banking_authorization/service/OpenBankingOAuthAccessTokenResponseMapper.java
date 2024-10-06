package allcount.poc.open_banking_authorization.service;

import allcount.poc.open_banking_authorization.entity.OpenBankingOAuthAccessTokenEntity;
import allcount.poc.open_banking_authorization.object.OpenBankingOAuthAccessTokenResponseDto;
import org.springframework.stereotype.Component;

@Component
public class OpenBankingOAuthAccessTokenResponseMapper {

    public OpenBankingOAuthAccessTokenResponseDto mapToOpenBankingOAuthAccessTokenResponse(OpenBankingOAuthAccessTokenEntity openBankingOAuthAccessToken) {
        OpenBankingOAuthAccessTokenResponseDto openBankingOAuthAccessTokenResponse = new OpenBankingOAuthAccessTokenResponseDto();
        openBankingOAuthAccessTokenResponse.setAccessToken(openBankingOAuthAccessToken.getAccessToken());
        openBankingOAuthAccessTokenResponse.setRefreshToken(openBankingOAuthAccessToken.getRefreshToken());
        openBankingOAuthAccessTokenResponse.setExpiresIn(openBankingOAuthAccessToken.getExpiresIn());
        openBankingOAuthAccessTokenResponse.setTokenType(openBankingOAuthAccessToken.getTokenType());
        openBankingOAuthAccessTokenResponse.setScope(openBankingOAuthAccessToken.getScope());
        openBankingOAuthAccessTokenResponse.setUserId(openBankingOAuthAccessToken.getUser().getId());
        openBankingOAuthAccessTokenResponse.setBank(openBankingOAuthAccessToken.getBank());
        return openBankingOAuthAccessTokenResponse;
    }
}
