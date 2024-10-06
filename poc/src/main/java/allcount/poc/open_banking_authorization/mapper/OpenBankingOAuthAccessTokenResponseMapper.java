package allcount.poc.open_banking_authorization.mapper;

import allcount.poc.open_banking_authorization.entity.OpenBankingOAuthAccessTokenEntity;
import allcount.poc.open_banking_authorization.entity.OpenBankingOAuthSessionEntity;
import allcount.poc.open_banking_authorization.object.OpenBankingOAuthAccessTokenResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Component;

@Component
public class OpenBankingOAuthAccessTokenResponseMapper {
    private static final String FIELD_ACCESS_TOKEN = "access_token";
    private static final String FIELD_REFRESH_TOKEN = "refresh_token";
    private static final String FIELD_EXPIRES_IN = "expires_in";
    private static final String FIELD_SCOPE = "scope";
    private static final String FIELD_TOKEN_TYPE = "token_type";


    public OpenBankingOAuthAccessTokenResponseDto mapToOpenBankingOAuthAccessTokenResponse(OpenBankingOAuthAccessTokenEntity openBankingOAuthAccessToken) {
        OpenBankingOAuthAccessTokenResponseDto openBankingOAuthAccessTokenResponse = new OpenBankingOAuthAccessTokenResponseDto();
        openBankingOAuthAccessTokenResponse.setId(openBankingOAuthAccessToken.getId());
        openBankingOAuthAccessTokenResponse.setAccessToken(openBankingOAuthAccessToken.getAccessToken());
        openBankingOAuthAccessTokenResponse.setRefreshToken(openBankingOAuthAccessToken.getRefreshToken());
        openBankingOAuthAccessTokenResponse.setExpiresIn(openBankingOAuthAccessToken.getExpiresIn());
        openBankingOAuthAccessTokenResponse.setTokenType(openBankingOAuthAccessToken.getTokenType());
        openBankingOAuthAccessTokenResponse.setScope(openBankingOAuthAccessToken.getScope());
        openBankingOAuthAccessTokenResponse.setUserId(openBankingOAuthAccessToken.getUser().getId());
        openBankingOAuthAccessTokenResponse.setBank(openBankingOAuthAccessToken.getBank());
        return openBankingOAuthAccessTokenResponse;
    }

    public OpenBankingOAuthAccessTokenEntity mapToOpenBankingOAuthAccessTokenEntity(
            Response response,
            OpenBankingOAuthSessionEntity session
    ) throws JsonProcessingException {
        String responseWithAccessToken = response.readEntity(String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = null;

        jsonNode = mapper.readTree(responseWithAccessToken);

        assert jsonNode != null;
        String accessToken = jsonNode.get(FIELD_ACCESS_TOKEN).textValue();
        String tokenType = jsonNode.get(FIELD_TOKEN_TYPE).textValue();
        Long expiresIn = jsonNode.get(FIELD_EXPIRES_IN).longValue();
        String refreshToken = jsonNode.get(FIELD_REFRESH_TOKEN).textValue();
        String scope = jsonNode.get(FIELD_SCOPE).textValue();

        OpenBankingOAuthAccessTokenEntity accessTokenEntity = new OpenBankingOAuthAccessTokenEntity();
        accessTokenEntity.setAccessToken(accessToken);
        accessTokenEntity.setRefreshToken(refreshToken);
        accessTokenEntity.setExpiresIn(expiresIn);
        accessTokenEntity.setTokenType(tokenType);
        accessTokenEntity.setScope(scope);
        accessTokenEntity.setUser(session.getUser());
        accessTokenEntity.setBank(session.getBank());

        return accessTokenEntity;
    }
}
