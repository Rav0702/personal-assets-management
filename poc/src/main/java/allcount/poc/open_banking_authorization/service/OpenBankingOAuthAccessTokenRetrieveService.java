package allcount.poc.open_banking_authorization.service;

import allcount.poc.open_banking_authorization.entity.OpenBankingOAuthAccessTokenEntity;
import allcount.poc.open_banking_authorization.entity.OpenBankingOAuthSessionEntity;
import allcount.poc.open_banking_authorization.mapper.OpenBankingOAuthAccessTokenResponseMapper;
import allcount.poc.open_banking_authorization.object.OpenBankingOAuthSessionStatusEnum;
import allcount.poc.open_banking_authorization.repository.OpenBankingOAuthAccessTokenRepository;
import allcount.poc.open_banking_authorization.repository.OpenBankingOAuthSessionRepository;
import allcount.poc.user.repository.AllcountUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    private transient final OpenBankingOAuthAccessTokenRepository openBankingOAuthAccessTokenRepository;
    private transient final OpenBankingOAuthAccessTokenResponseMapper openBankingOAuthAccessTokenResponseMapper;

    @Autowired
    public OpenBankingOAuthAccessTokenRetrieveService(
            AllcountUserRepository userRepository,
            OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository,
            OpenBankingOAuthAccessTokenRepository openBankingOAuthAccessTokenRepository, OpenBankingOAuthAccessTokenResponseMapper openBankingOAuthAccessTokenResponseMapper
    ) {
        super(userRepository, openBankingOAuthSessionRepository);
        this.openBankingOAuthAccessTokenRepository = openBankingOAuthAccessTokenRepository;
        this.openBankingOAuthAccessTokenResponseMapper = openBankingOAuthAccessTokenResponseMapper;
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
        Form form = determineAccessTokenRequestBodyForm(code, codeVerifier);

        return client
                .property(ClientProperties.FOLLOW_REDIRECTS, Boolean.FALSE)
                .target(BASE_URL + TOKEN_URL)
                .request()
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
    }

    private Form determineAccessTokenRequestBodyForm(String code, String codeVerifier) {
        Form form = new Form();
        form.param(PARAM_GRANT_TYPE, GRANT_TYPE_AUTHORIZATION_CODE);
        form.param(PARAM_CODE, code);
        form.param(PARAM_CODE_VERIFIER, codeVerifier);
        form.param(PARAM_CLIENT_ID, simulationClientId);
        form.param(PARAM_REDIRECT_URI, REDIRECT_URI);
        return form;
    }

    private void updateOpenBankingOAuthSessionAccessTokenReceived(OpenBankingOAuthSessionEntity session) {
        session.setStatus(OpenBankingOAuthSessionStatusEnum.ACCESS_TOKEN_RECEIVED);

        openBankingOAuthSessionRepository.save(session);
    }

    private OpenBankingOAuthAccessTokenEntity parseOpenBankingOAuthAccessTokenFromOpenBankingResponse(
            Response response,
            OpenBankingOAuthSessionEntity session
    ) throws JsonProcessingException {
        return openBankingOAuthAccessTokenRepository.save(
                openBankingOAuthAccessTokenResponseMapper.mapToOpenBankingOAuthAccessTokenEntity(response, session)
        );
    }
}
