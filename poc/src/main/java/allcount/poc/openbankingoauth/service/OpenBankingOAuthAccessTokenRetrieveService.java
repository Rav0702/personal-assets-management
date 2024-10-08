package allcount.poc.openbankingoauth.service;

import allcount.poc.openbankingoauth.entity.OpenBankingOAuthAccessTokenEntity;
import allcount.poc.openbankingoauth.entity.OpenBankingOAuthSessionEntity;
import allcount.poc.openbankingoauth.mapper.OpenBankingBankToBaseUriMapper;
import allcount.poc.openbankingoauth.mapper.OpenBankingOAuthAccessTokenResponseMapper;
import allcount.poc.openbankingoauth.object.enums.OpenBankingOAuthSessionStatusEnum;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthAccessTokenRepository;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthSessionRepository;
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

/**
 * Service for retrieving the OpenBankingOAuthAccessTokenEntity.
 */
@Service
public class OpenBankingOAuthAccessTokenRetrieveService extends OpenBankingOAuthService {
    private static final String PARAM_GRANT_TYPE = "grant_type";
    private static final String PARAM_CODE = "code";
    private static final String PARAM_CODE_VERIFIER = "code_verifier";
    private static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";

    private static final String TOKEN_URL = "/gw/oidc/token";

    private final transient OpenBankingOAuthAccessTokenRepository openBankingOAuthAccessTokenRepository;
    private final transient OpenBankingOAuthAccessTokenResponseMapper openBankingOAuthAccessTokenResponseMapper;

    /**
     * Constructor.
     *
     * @param userRepository                            - the AllcountUserRepository
     * @param openBankingOAuthSessionRepository         - the OpenBankingOAuthSessionRepository
     * @param openBankingOAuthAccessTokenRepository     - the OpenBankingOAuthAccessTokenRepository
     * @param openBankingOAuthAccessTokenResponseMapper - the OpenBankingOAuthAccessTokenResponseMapper
     */
    @Autowired
    public OpenBankingOAuthAccessTokenRetrieveService(
            AllcountUserRepository userRepository,
            OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository,
            OpenBankingOAuthAccessTokenRepository openBankingOAuthAccessTokenRepository,
            OpenBankingOAuthAccessTokenResponseMapper openBankingOAuthAccessTokenResponseMapper,
            OpenBankingBankToBaseUriMapper openBankingBankToBaseUriMapper
    ) {
        super(userRepository, openBankingOAuthSessionRepository, openBankingBankToBaseUriMapper);
        this.openBankingOAuthAccessTokenRepository = openBankingOAuthAccessTokenRepository;
        this.openBankingOAuthAccessTokenResponseMapper = openBankingOAuthAccessTokenResponseMapper;
    }

    /**
     * Retrieves the OpenBankingOAuthAccessTokenEntity.
     *
     * @param code  - the code
     * @param state - the state
     * @return the OpenBankingOAuthAccessTokenEntity
     * @throws JsonProcessingException - if the response cannot be processed
     */
    @Transactional
    public OpenBankingOAuthAccessTokenEntity retrieveOpenBankingOAuthAccessToken(String code, UUID state)
            throws JsonProcessingException {
        OpenBankingOAuthSessionEntity session = openBankingOAuthSessionRepository
                .findByStateAndStatus(state, OpenBankingOAuthSessionStatusEnum.OAUTH_URI_GENERATED)
                .orElseThrow();

        Response response = requestAccessTokensFromCode(code, session);

        updateOpenBankingOAuthSessionAccessTokenReceived(session);

        return parseOpenBankingOAuthAccessTokenFromOpenBankingResponse(response, session);
    }

    /**
     * Requests the access tokens from the code.
     *
     * @param code    - the code
     * @param session - the OpenBankingOAuthSessionEntity
     * @return the Response
     */
    private Response requestAccessTokensFromCode(String code, OpenBankingOAuthSessionEntity session) {
        Form form = determineAccessTokenRequestBodyForm(code, session.getCodeVerifier());

        return client
                .property(ClientProperties.FOLLOW_REDIRECTS, Boolean.FALSE)
                .target(openBankingBankToBaseUriMapper.getBaseUri(session.getBank()) + TOKEN_URL)
                .request()
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
    }

    /**
     * Determines the access token request body form.
     *
     * @param code         - the code
     * @param codeVerifier - the code verifier
     * @return the Form
     */
    private Form determineAccessTokenRequestBodyForm(String code, String codeVerifier) {
        Form form = new Form();
        form.param(PARAM_GRANT_TYPE, GRANT_TYPE_AUTHORIZATION_CODE);
        form.param(PARAM_CODE, code);
        form.param(PARAM_CODE_VERIFIER, codeVerifier);
        form.param(PARAM_CLIENT_ID, simulationClientId);
        form.param(PARAM_REDIRECT_URI, REDIRECT_URI);
        return form;
    }

    /**
     * Updates the OpenBankingOAuthSessionEntity to have the access token received status.
     *
     * @param session - the OpenBankingOAuthSessionEntity
     */
    private void updateOpenBankingOAuthSessionAccessTokenReceived(OpenBankingOAuthSessionEntity session) {
        session.setStatus(OpenBankingOAuthSessionStatusEnum.ACCESS_TOKEN_RECEIVED);

        openBankingOAuthSessionRepository.save(session);
    }

    /**
     * Parses the OpenBankingOAuthAccessTokenEntity from the OpenBanking response.
     *
     * @param response - the Response
     * @param session  - the OpenBankingOAuthSessionEntity
     * @return the OpenBankingOAuthAccessTokenEntity
     * @throws JsonProcessingException - if the response cannot be processed
     */
    private OpenBankingOAuthAccessTokenEntity parseOpenBankingOAuthAccessTokenFromOpenBankingResponse(
            Response response,
            OpenBankingOAuthSessionEntity session
    ) throws JsonProcessingException {
        return openBankingOAuthAccessTokenRepository.save(
                openBankingOAuthAccessTokenResponseMapper.mapToOpenBankingOAuthAccessTokenEntity(response, session)
        );
    }
}
