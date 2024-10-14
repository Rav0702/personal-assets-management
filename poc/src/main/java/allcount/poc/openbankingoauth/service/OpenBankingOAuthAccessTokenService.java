package allcount.poc.openbankingoauth.service;

import allcount.poc.openbankingoauth.entity.OpenBankingOAuthAccessTokenRedisEntity;
import allcount.poc.openbankingoauth.entity.OpenBankingOAuthRefreshTokenEntity;
import allcount.poc.openbankingoauth.mapper.OpenBankingBankToBaseUriMapper;
import allcount.poc.openbankingoauth.mapper.OpenBankingOAuthAccessTokenResponseMapper;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthAccessTokenRedisRepository;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthRefreshTokenRepository;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthSessionRepository;
import allcount.poc.user.entity.AllcountUser;
import allcount.poc.user.repository.AllcountUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base service for the OpenBankingOAuthAccessTokenEntity.
 */
@Service
public class OpenBankingOAuthAccessTokenService extends OpenBankingOAuthService {
    protected static final String HEADER_AUTHORIZATION = "Authorization";
    protected static final String PARAM_GRANT_TYPE = "grant_type";
    protected static final String PARAM_CODE = "code";
    protected static final String PARAM_CODE_VERIFIER = "code_verifier";
    protected static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
    protected static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";

    protected static final String TOKEN_URL = "/gw/oidc/token";

    private static final String CREDENTIAL_SEPARATOR = ":";
    private static final String AUTHENTICATION_TYPE_BASIC = "Basic ";

    protected final transient OpenBankingOAuthRefreshTokenRepository openBankingOAuthRefreshTokenRepository;
    protected final transient OpenBankingOAuthAccessTokenResponseMapper openBankingOAuthAccessTokenResponseMapper;
    protected final transient OpenBankingOAuthAccessTokenRedisRepository openBankingOAuthAccessTokenRedisRepository;

    /**
     * Constructor.
     *
     * @param userRepository                            - the AllcountUserRepository
     * @param openBankingOAuthSessionRepository         - the OpenBankingOAuthSessionRepository
     * @param openBankingOAuthRefreshTokenRepository    - the OpenBankingOAuthRefreshTokenRepository
     * @param openBankingOAuthAccessTokenResponseMapper - the OpenBankingOAuthAccessTokenResponseMapper
     */
    @Autowired
    public OpenBankingOAuthAccessTokenService(
            UserDetailsService userDetailsService,
            AllcountUserRepository userRepository,
            OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository,
            OpenBankingOAuthAccessTokenRedisRepository openBankingOAuthAccessTokenRedisRepository,
            OpenBankingOAuthRefreshTokenRepository openBankingOAuthRefreshTokenRepository,
            OpenBankingOAuthAccessTokenResponseMapper openBankingOAuthAccessTokenResponseMapper,
            OpenBankingBankToBaseUriMapper openBankingBankToBaseUriMapper
    ) {
        super(userDetailsService, userRepository, openBankingOAuthSessionRepository, openBankingBankToBaseUriMapper);
        this.openBankingOAuthRefreshTokenRepository = openBankingOAuthRefreshTokenRepository;
        this.openBankingOAuthAccessTokenResponseMapper = openBankingOAuthAccessTokenResponseMapper;
        this.openBankingOAuthAccessTokenRedisRepository = openBankingOAuthAccessTokenRedisRepository;
    }

    /**
     * Generates all entities based on AccessToken response.
     *
     * @param response - the response
     * @param user - the user
     * @param bank - the bank
     * @return the OpenBankingOAuthAccessTokenEntity
     * @throws JsonProcessingException - if the response cannot be processed
     */
    @Transactional
    protected OpenBankingOAuthAccessTokenRedisEntity generateAllOpenBankingOAuthAccessTokenEntity(
            Response response,
            AllcountUser user,
            OpenBankingBankEnum bank
    ) throws JsonProcessingException {
        String responseWithAccessToken = response.readEntity(String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(responseWithAccessToken);

        OpenBankingOAuthRefreshTokenEntity refreshToken =
                openBankingOAuthAccessTokenResponseMapper.mapResponseToOAuthRefreshToken(jsonNode, user, bank);

        openBankingOAuthRefreshTokenRepository.save(refreshToken);

        OpenBankingOAuthAccessTokenRedisEntity accessToken =
                openBankingOAuthAccessTokenResponseMapper.mapResponseToOAuthAccessToken(jsonNode, refreshToken);

        openBankingOAuthAccessTokenRedisRepository.save(accessToken);

        return accessToken;
    }

    /**
     * Determines authorization header.
     *
     * @return the authorization header value.
     */
    protected String determineAuthorizationHeader() {
        String credentials = simulationClientId + CREDENTIAL_SEPARATOR + simulationClientSecret;

        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        return AUTHENTICATION_TYPE_BASIC + encodedCredentials;
    }
}
