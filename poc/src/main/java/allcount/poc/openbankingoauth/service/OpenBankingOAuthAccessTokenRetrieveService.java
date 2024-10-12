package allcount.poc.openbankingoauth.service;

import static java.time.LocalDateTime.now;
import allcount.poc.openbankingoauth.entity.OpenBankingOAuthAccessTokenEntity;
import allcount.poc.openbankingoauth.entity.OpenBankingOAuthAccessTokenRedisEntity;
import allcount.poc.openbankingoauth.entity.OpenBankingOAuthSessionEntity;
import allcount.poc.openbankingoauth.mapper.OpenBankingBankToBaseUriMapper;
import allcount.poc.openbankingoauth.mapper.OpenBankingOAuthAccessTokenResponseMapper;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.object.enums.OpenBankingOAuthSessionStatusEnum;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthAccessTokenRedisRepository;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthAccessTokenRepository;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthSessionRepository;
import allcount.poc.user.entity.AllcountUser;
import allcount.poc.user.repository.AllcountUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import org.glassfish.jersey.client.ClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for retrieving the OpenBankingOAuthAccessTokenEntity.
 */
@Service
public class OpenBankingOAuthAccessTokenRetrieveService extends OpenBankingOAuthService {
    private static final String PARAM_GRANT_TYPE = "grant_type";
    private static final String PARAM_CODE = "code";
    private static final String GRAND_TYPE_REFRESH_TOKEN = "refresh_token";
    private static final String PARAM_CODE_VERIFIER = "code_verifier";
    private static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";

    private static final String TOKEN_URL = "/gw/oidc/token";

    private final transient OpenBankingOAuthAccessTokenRepository openBankingOAuthAccessTokenRepository;
    private final transient OpenBankingOAuthAccessTokenResponseMapper openBankingOAuthAccessTokenResponseMapper;
    private final transient OpenBankingOAuthAccessTokenRedisRepository openBankingOAuthAccessTokenRedisRepository;
    private static final Logger LOG = Logger.getLogger(OpenBankingOAuthAccessTokenRetrieveService.class.getName());

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
            UserDetailsService userDetailsService,
            AllcountUserRepository userRepository,
            OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository,
            OpenBankingOAuthAccessTokenRedisRepository openBankingOAuthAccessTokenRedisRepository,
            OpenBankingOAuthAccessTokenRepository openBankingOAuthAccessTokenRepository,
            OpenBankingOAuthAccessTokenResponseMapper openBankingOAuthAccessTokenResponseMapper,
            OpenBankingBankToBaseUriMapper openBankingBankToBaseUriMapper
    ) {
        super(userDetailsService, userRepository, openBankingOAuthSessionRepository, openBankingBankToBaseUriMapper);
        this.openBankingOAuthAccessTokenRepository = openBankingOAuthAccessTokenRepository;
        this.openBankingOAuthAccessTokenResponseMapper = openBankingOAuthAccessTokenResponseMapper;
        this.openBankingOAuthAccessTokenRedisRepository = openBankingOAuthAccessTokenRedisRepository;
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

        OpenBankingOAuthAccessTokenEntity token =
                parseOpenBankingOAuthAccessTokenFromOpenBankingResponse(response, session);
        addOpenBankingOAuthAccessTokenToRedis(token);

        return token;
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

    /**
     * Parses the OpenBankingOAuthRefreshTokenEntity from the OpenBanking response.
     *
     * @param response - the Response
     * @param bank     - the bank
     * @param user     - the user
     * @return the OpenBankingOAuthAccessTokenEntity
     * @throws JsonProcessingException - if the response cannot be processed
     */
    private OpenBankingOAuthAccessTokenEntity parseOpenBankingOAuthRefreshTokenFromOpenBankingResponse(
            Response response,
            OpenBankingBankEnum bank,
            AllcountUser user
    ) throws JsonProcessingException {
        return openBankingOAuthAccessTokenRepository.save(
                openBankingOAuthAccessTokenResponseMapper.mapToOpenBankingOAuthRefreshTokenEntity(response, bank, user)
        );
    }

    public String getAccessToken(UUID userId) throws JsonProcessingException {
        Optional<OpenBankingOAuthAccessTokenEntity> latestToken = openBankingOAuthAccessTokenRepository.findFirstByUserIdOrderByStartDateTimeDesc(userId);

        if (latestToken.isPresent()) {
            if (latestToken.get().getEndDateTime().isAfter(now().minusMinutes(1))) {
                AllcountUser user = userRepository.findById(userId).orElseThrow();
                OpenBankingOAuthAccessTokenEntity refreshedToken = refreshAccessToken(latestToken.get(),
                        latestToken.get().getBank(),
                        user);
                openBankingOAuthAccessTokenRepository.save(refreshedToken);
                return refreshedToken.getAccessToken();
            } else {
                return latestToken.get().getAccessToken();
            }
        } else {
            throw new RuntimeException("No access token found for user with id: " + userId);
        }
    }

    private OpenBankingOAuthAccessTokenEntity refreshAccessToken(OpenBankingOAuthAccessTokenEntity token,
                                                                 OpenBankingBankEnum bank,
                                                                 AllcountUser user) throws JsonProcessingException {
        Form form = determineRefreshTokenRequestBodyForm(token.getRefreshToken());

        Response response = client
                .property(ClientProperties.FOLLOW_REDIRECTS, Boolean.FALSE)
                .target(openBankingBankToBaseUriMapper.getBaseUri(token.getBank()) + TOKEN_URL)
                .request()
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

        return parseOpenBankingOAuthRefreshTokenFromOpenBankingResponse(response, bank, user);
    }

    /**
     * Determines the access token request body form.
     *
     * @param refreshToken - the refresh token we use to refresh the access token
     * @return the Form
     */
    private Form determineRefreshTokenRequestBodyForm(String refreshToken) {
        Form form = new Form();
        form.param(PARAM_GRANT_TYPE, GRAND_TYPE_REFRESH_TOKEN);
        form.param(PARAM_CODE, refreshToken);
        form.param(PARAM_CLIENT_ID, simulationClientId);
        form.param(PARAM_REDIRECT_URI, REDIRECT_URI);
        return form;
    }

    private void addOpenBankingOAuthAccessTokenToRedis(OpenBankingOAuthAccessTokenEntity token) {
        OpenBankingOAuthAccessTokenRedisEntity redisToken = new OpenBankingOAuthAccessTokenRedisEntity();

        String identifier = token.getBank().name() + "|" + token.getUser().getId().toString();
        redisToken.setId(identifier);
        redisToken.setAccessToken(token.getAccessToken());
        redisToken.setExpiresIn(token.getExpiresIn());

        LOG.info("Saving token to redis with key: " + redisToken.getId());

        openBankingOAuthAccessTokenRedisRepository.save(redisToken);
    }
}
