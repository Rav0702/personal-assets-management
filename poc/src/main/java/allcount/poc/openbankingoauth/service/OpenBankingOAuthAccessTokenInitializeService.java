package allcount.poc.openbankingoauth.service;

import allcount.poc.openbankingoauth.entity.OpenBankingOAuthAccessTokenRedisEntity;
import allcount.poc.openbankingoauth.entity.OpenBankingSessionEntity;
import allcount.poc.openbankingoauth.mapper.OpenBankingBankToBaseUriMapper;
import allcount.poc.openbankingoauth.mapper.OpenBankingBankToRefreshTokenPathUriMapper;
import allcount.poc.openbankingoauth.mapper.OpenBankingBankToSimulationMapper;
import allcount.poc.openbankingoauth.mapper.OpenBankingOAuthAccessTokenResponseMapper;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
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
 * Service for retrieving the OpenBankingRefreshTokenEntity.
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
    public OpenBankingOAuthAccessTokenInitializeService(
            UserDetailsService userDetailsService,
            AllcountUserRepository userRepository,
            OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository,
            OpenBankingOAuthAccessTokenRedisRepository openBankingOAuthAccessTokenRedisRepository,
            OpenBankingOAuthRefreshTokenRepository openBankingOAuthRefreshTokenRepository,
            OpenBankingOAuthAccessTokenResponseMapper openBankingOAuthAccessTokenResponseMapper,
            OpenBankingBankToBaseUriMapper openBankingBankToBaseUriMapper,
            OpenBankingBankToRefreshTokenPathUriMapper openBankingBankToRefreshTokenPathUriMapper,
            OpenBankingBankToSimulationMapper openBankingBankToSimulationMapper
    ) {
        super(userDetailsService, userRepository, openBankingOAuthSessionRepository,
                openBankingOAuthAccessTokenRedisRepository, openBankingOAuthRefreshTokenRepository,
                openBankingOAuthAccessTokenResponseMapper, openBankingBankToBaseUriMapper, openBankingBankToRefreshTokenPathUriMapper,
                openBankingBankToSimulationMapper);
    }


    /**
     * Retrieves the OpenBankingRefreshTokenEntity.
     *
     * @param code  - the code
     * @param state - the state
     * @return the OpenBankingRefreshTokenEntity
     * @throws JsonProcessingException - if the response cannot be processed
     */
    @Transactional
    public OpenBankingOAuthAccessTokenRedisEntity initializeOpenBankingOAuthAccessToken(String code, UUID state)
            throws JsonProcessingException {
        OpenBankingSessionEntity session = openBankingOAuthSessionRepository
                .findByStateAndStatus(state, OpenBankingOAuthSessionStatusEnum.OAUTH_URI_GENERATED)
                .orElseThrow();

        Response response = requestAccessTokenFromCode(code, session);

        updateOpenBankingOAuthSessionAccessTokenReceived(session);

        return generateAllOpenBankingOAuthAccessTokenEntity(response, session.getUser(), session.getBank());
    }

    /**
     * Requests the access tokens from the code.
     *
     * @param code    - the code
     * @param session - the OpenBankingSessionEntity
     * @return the Response
     */
    private Response requestAccessTokenFromCode(String code, OpenBankingSessionEntity session) {
        OpenBankingBankEnum bank = session.getBank();

        Form form = determineAccessTokenRequestBodyForm(code, session.getCodeVerifier(), bank);
        String requestUri = openBankingBankToBaseUriMapper.getBaseUri(bank)
                + super.openBankingBankToRefreshTokenPathUriMapper.getTokenRefreshPathUri(bank);

        return client
                .property(ClientProperties.FOLLOW_REDIRECTS, Boolean.FALSE)
                .target(requestUri)
                .request()
                .header(HEADER_AUTHORIZATION, determineAuthorizationHeader(bank))
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
    }

    /**
     * Determines the access token request body form.
     *
     * @param code         - the code
     * @param codeVerifier - the code verifier
     * @return the Form
     */
    private Form determineAccessTokenRequestBodyForm(String code, String codeVerifier, OpenBankingBankEnum bank) {
        Form form = new Form();
        form.param(PARAM_GRANT_TYPE, GRANT_TYPE_AUTHORIZATION_CODE);
        form.param(PARAM_CODE, code);
        form.param(PARAM_CODE_VERIFIER, codeVerifier);
        form.param(PARAM_CLIENT_ID, openBankingBankToSimulationMapper.mapToSimulationId(bank));
        form.param(PARAM_REDIRECT_URI, REDIRECT_URI);

        return form;
    }

    /**
     * Updates the OpenBankingSessionEntity to have the access token received status.
     *
     * @param session - the OpenBankingSessionEntity
     */
    private void updateOpenBankingOAuthSessionAccessTokenReceived(OpenBankingSessionEntity session) {
        session.setStatus(OpenBankingOAuthSessionStatusEnum.ACCESS_TOKEN_RECEIVED);

        openBankingOAuthSessionRepository.save(session);
    }
}
