package allcount.poc.openbankingoauth.service;

import allcount.poc.openbankingoauth.entity.OpenBankingOAuthAccessTokenRedisEntity;
import allcount.poc.openbankingoauth.entity.OpenBankingRefreshTokenEntity;
import allcount.poc.openbankingoauth.mapper.OpenBankingBankToBaseUriMapper;
import allcount.poc.openbankingoauth.mapper.OpenBankingBankToRefreshTokenPathUriMapper;
import allcount.poc.openbankingoauth.mapper.OpenBankingBankToSimulationMapper;
import allcount.poc.openbankingoauth.mapper.OpenBankingOAuthAccessTokenResponseMapper;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthAccessTokenRedisRepository;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthRefreshTokenRepository;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthSessionRepository;
import allcount.poc.user.entity.AllcountUser;
import allcount.poc.user.repository.AllcountUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientProperties;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for refreshing the OpenBankingOAuthAccessTokenEntity.
 */
@Service
public class OpenBankingOAuthAccessTokenRefreshService extends OpenBankingOAuthAccessTokenInitializeService {
    private static final String PARAM_REFRESH_TOKEN = "refresh_token";

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
    public OpenBankingOAuthAccessTokenRefreshService(
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
                openBankingOAuthAccessTokenResponseMapper, openBankingBankToBaseUriMapper,
                openBankingBankToRefreshTokenPathUriMapper, openBankingBankToSimulationMapper
        );
    }

    /**
     * Refreshes the access token.
     *
     * @param user - the user
     * @param bank - the bank
     * @return the OpenBankingOAuthAccessTokenRedisEntity
     * @throws JsonProcessingException - if the response cannot be processed
     */
    @Transactional
    public OpenBankingOAuthAccessTokenRedisEntity refreshAccessToken(AllcountUser user, OpenBankingBankEnum bank)
            throws JsonProcessingException {
        OpenBankingRefreshTokenEntity refreshToken = openBankingOAuthRefreshTokenRepository
                .findFirstByUserIdAndBankOrderByCreatedTimestampDesc(user.getId(), bank)
                .orElseThrow();

        Response response = requestTokenRefresh(refreshToken, bank);

        return generateAllOpenBankingOAuthAccessTokenEntity(response, user, refreshToken.getBank());
    }

    /**
     * Gets the refresh token response from api.
     *
     * @param refreshToken - the OpenBankingRefreshTokenEntity
     * @return response
     */
    private Response requestTokenRefresh(OpenBankingRefreshTokenEntity refreshToken, OpenBankingBankEnum bank) {
        Form form = determineRefreshTokenRequestBodyForm(refreshToken);

        return client
                .property(ClientProperties.FOLLOW_REDIRECTS, Boolean.FALSE)
                .target(openBankingBankToBaseUriMapper.getBaseUri(refreshToken.getBank())
                       + openBankingBankToRefreshTokenPathUriMapper.getTokenRefreshPathUri(bank))
                .request()
                .header(HEADER_AUTHORIZATION, determineAuthorizationHeader(bank))
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
    }

    /**
     * Determines the access token request body form.
     *
     * @param token - the initial OpenBankingRefreshTokenEntity
     * @return the Form
     */
    private Form determineRefreshTokenRequestBodyForm(OpenBankingRefreshTokenEntity token) {
        Form form = new Form();
        form.param(PARAM_GRANT_TYPE, GRANT_TYPE_REFRESH_TOKEN);
        form.param(PARAM_REFRESH_TOKEN, token.getRefreshToken());
        form.param(PARAM_SCOPE, token.getScope());

        return form;
    }
}
