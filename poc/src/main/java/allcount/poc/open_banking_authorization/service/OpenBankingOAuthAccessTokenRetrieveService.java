package allcount.poc.open_banking_authorization.service;

import allcount.poc.open_banking_authorization.entity.OpenBankingOAuthAccessTokenEntity;
import allcount.poc.open_banking_authorization.entity.OpenBankingOAuthSessionEntity;
import allcount.poc.open_banking_authorization.object.OpenBankingOAuthSessionStatusEnum;
import allcount.poc.open_banking_authorization.repository.OpenBankingOAuthAccessTokenRepository;
import allcount.poc.open_banking_authorization.repository.OpenBankingOAuthSessionRepository;
import allcount.poc.user.repository.AllcountUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.UUID;
import org.glassfish.jersey.client.ClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OpenBankingOAuthAccessTokenRetrieveService extends OpenBankingOAuthService {
    private static final String PARAM_GRANT_TYPE = "grant_type";
    private static final String PARAM_CODE = "code";
    private static final String PARAM_CODE_VERIFIER = "code_verifier";
    private static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";

    private static final String TOKEN_URL = "/gw/oidc/token";

    private static final String FIELD_ACCESS_TOKEN = "access_token";
    private static final String FIELD_REFRESH_TOKEN = "refresh_token";
    private static final String FIELD_EXPIRES_IN = "expires_in";
    private static final String FIELD_SCOPE = "scope";
    private static final String FIELD_TOKEN_TYPE = "token_type";

    private transient final OpenBankingOAuthAccessTokenRepository openBankingOAuthAccessTokenRepository;

    @Autowired
    public OpenBankingOAuthAccessTokenRetrieveService(
            AllcountUserRepository userRepository,
            OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository,
            OpenBankingOAuthAccessTokenRepository openBankingOAuthAccessTokenRepository
    ) {
        super(userRepository, openBankingOAuthSessionRepository);
        this.openBankingOAuthAccessTokenRepository = openBankingOAuthAccessTokenRepository;
    }

    @Transactional
    public OpenBankingOAuthAccessTokenEntity retrieveOpenBankingOAuthAccessToken(String code, UUID state) throws JsonProcessingException {
        OpenBankingOAuthSessionEntity session = openBankingOAuthSessionRepository
                .findByStateAndStatus(state, OpenBankingOAuthSessionStatusEnum.OAUTH_URI_GENERATED)
                .orElseThrow();

        Response response = requestAccessTokensFromCode(code, session.getCodeVerifier());

        updateOpenBankingOAuthSessionAccessTokenReceived(session);

        return parseOpenBankingOAuthAccessTokenFromOpenBankingResponse(response, session);
    }


    private Response requestAccessTokensFromCode(String code, String codeVerifier) {
        Form form = new Form();
        form.param(PARAM_GRANT_TYPE, GRANT_TYPE_AUTHORIZATION_CODE);
        form.param(PARAM_CODE, code);
        form.param(PARAM_CODE_VERIFIER, codeVerifier);
        form.param(PARAM_CLIENT_ID, simulationClientId);
        form.param(PARAM_REDIRECT_URI, REDIRECT_URI);

        return client
                .property(ClientProperties.FOLLOW_REDIRECTS, Boolean.FALSE)
                .target(BASE_URL + TOKEN_URL)
                .request()
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
    }

    private OpenBankingOAuthAccessTokenEntity parseOpenBankingOAuthAccessTokenFromOpenBankingResponse(Response response, OpenBankingOAuthSessionEntity session)
            throws JsonProcessingException {
        String responseWithAccessToken  = response.readEntity(String.class);

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

        return openBankingOAuthAccessTokenRepository.save(accessTokenEntity);
    }

    private void updateOpenBankingOAuthSessionAccessTokenReceived(OpenBankingOAuthSessionEntity session) {
        session.setStatus(OpenBankingOAuthSessionStatusEnum.ACCESS_TOKEN_RECEIVED);

        openBankingOAuthSessionRepository.save(session);
    }
}
