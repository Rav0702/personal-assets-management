package allcount.poc.openbankingoauth.service;

import allcount.poc.openbankingoauth.entity.OpenBankingOAuthAccessTokenRedisEntity;
import allcount.poc.openbankingoauth.entity.OpenBankingOAuthSessionEntity;
import allcount.poc.openbankingoauth.mapper.OpenBankingBankToBaseUriMapper;
import allcount.poc.openbankingoauth.mapper.OpenBankingOAuthAccessTokenResponseMapper;
import allcount.poc.openbankingoauth.object.enums.OpenBankingOAuthSessionStatusEnum;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthAccessTokenRedisRepository;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthRefreshTokenRepository;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthSessionRepository;
import allcount.poc.user.repository.AllcountUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.UUID;
import org.glassfish.jersey.client.ClientProperties;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for retrieving the OpenBankingOAuthRefreshTokenEntity.
 */
@Service
public class OpenBankingOAuthAccessTokenInitializeService extends OpenBankingOAuthAccessTokenService {
    /**
     * Constructor.
     *
     * @param userDetailsService                         - the UserDetailsService
     * @param userRepository                             - the AllcountUserRepository
     * @param openBankingOAuthSessionRepository          - the OpenBankingOAuthSessionRepository
     * @param openBankingOAuthAccessTokenRedisRepository - the OpenBankingOAuthAccessTokenRedisRepository
     * @param openBankingOAuthRefreshTokenRepository     - the OpenBankingOAuthRefreshTokenRepository
     * @param openBankingOAuthAccessTokenResponseMapper  - the OpenBankingOAuthAccessTokenResponseMapper
     * @param openBankingBankToBaseUriMapper             - the OpenBankingBankToBaseUriMapper
     */
    public OpenBankingOAuthAccessTokenInitializeService(UserDetailsService userDetailsService,
                                                        AllcountUserRepository userRepository,
                                                        OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository,
                                                        OpenBankingOAuthAccessTokenRedisRepository openBankingOAuthAccessTokenRedisRepository,
                                                        OpenBankingOAuthRefreshTokenRepository openBankingOAuthRefreshTokenRepository,
                                                        OpenBankingOAuthAccessTokenResponseMapper openBankingOAuthAccessTokenResponseMapper,
                                                        OpenBankingBankToBaseUriMapper openBankingBankToBaseUriMapper) {
        super(userDetailsService, userRepository, openBankingOAuthSessionRepository,
                openBankingOAuthAccessTokenRedisRepository, openBankingOAuthRefreshTokenRepository,
                openBankingOAuthAccessTokenResponseMapper, openBankingBankToBaseUriMapper);
    }


    /**
     * Retrieves the OpenBankingOAuthRefreshTokenEntity.
     *
     * @param code  - the code
     * @param state - the state
     * @return the OpenBankingOAuthRefreshTokenEntity
     * @throws JsonProcessingException - if the response cannot be processed
     */
    @Transactional
    public OpenBankingOAuthAccessTokenRedisEntity initializeOpenBankingOAuthAccessToken(String code, UUID state)
            throws JsonProcessingException {
        OpenBankingOAuthSessionEntity session = openBankingOAuthSessionRepository
                .findByStateAndStatus(state, OpenBankingOAuthSessionStatusEnum.OAUTH_URI_GENERATED)
                .orElseThrow();

        Response response = requestAccessTokenFromCode(code, session);

        updateOpenBankingOAuthSessionAccessTokenReceived(session);

        return populateOpenBankingOAuthAccessTokenFromResponse(response, session.getUser(), session.getBank());
    }

    /**
     * Requests the access tokens from the code.
     *
     * @param code    - the code
     * @param session - the OpenBankingOAuthSessionEntity
     * @return the Response
     */
    private Response requestAccessTokenFromCode(String code, OpenBankingOAuthSessionEntity session) {
        Form form = determineAccessTokenRequestBodyForm(code, session.getCodeVerifier());

        return client
                .property(ClientProperties.FOLLOW_REDIRECTS, Boolean.FALSE)
                .target(openBankingBankToBaseUriMapper.getBaseUri(session.getBank()) + TOKEN_URL)
                .request()
                .header(HEADER_AUTHORIZATION, determineAuthorizationHeader())
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
}
