package allcount.poc.openbankingoauth.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import allcount.poc.core.test.integration.IntegrationTest;
import allcount.poc.core.test.integration.IntegrationTestLib;
import allcount.poc.openbankingoauth.entity.OpenBankingOAuthAccessTokenRedisEntity;
import allcount.poc.openbankingoauth.entity.OpenBankingSessionEntity;
import allcount.poc.openbankingoauth.mapper.OpenBankingBankToBaseUriMapper;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.object.enums.OpenBankingOAuthSessionStatusEnum;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthAccessTokenRedisRepository;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthRefreshTokenRepository;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthSessionRepository;
import allcount.poc.openbankingoauth.service.OpenBankingOAuthAccessTokenDetermineService;
import allcount.poc.user.entity.AllcountUser;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration test for initializing the OpenBankingOAuthSession.
 */
@ActiveProfiles("open-banking-integration-test")
public class OpenBankingOAuthInitializationIntegrationTest extends IntegrationTest {
    private static final String ENDPOINT_USER_CREATE = "v1/open-banking-authorization/{userId}/initialize-session";
    private static final String ENDPOINT_RETRIEVE_ACCESS_TOKEN = "v1/open-banking-authorization/retrieve-access-token";
    private static final String ENDPOINT_TOKEN = "/gw/oidc/token";

    private static final String PARAM_BANK = "bank";
    private static final String PARAM_CODE = "code";
    private static final String PARAM_STATE = "state";

    private static final String FIELD_STATUS = "status";

    private static final String TEST_CODE = "testCode";
    private static final String PATH_RELATIVE =
            String.join(File.separator,
                    List.of("src", "test", "java", "allcount", "poc", "openbankingoauth", "integration"));
    private static final String FILE_ACCESS_TOKEN_MOCK_RESPONSE_SUCCESSFUL =
            PATH_RELATIVE + File.separator + "response" + File.separator + "accessTokenMockResponse.json";
    private static final String FILE_ACCESS_TOKEN_MOCK_RESPONSE_EXPIRY_INSTANT =
            PATH_RELATIVE + File.separator + "response" + File.separator + "accessTokenMockResponseExpiryInstant.json";

    private final transient OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository;
    private final transient OpenBankingOAuthRefreshTokenRepository openBankingOAuthRefreshTokenRepository;
    private final transient OpenBankingBankToBaseUriMapper openBankingBankToBaseUriMapper;
    private final transient OpenBankingOAuthAccessTokenRedisRepository openBankingOAuthAccessTokenRedisRepository;
    private final transient OpenBankingOAuthAccessTokenDetermineService openBankingOAuthAccessTokenDetermineService;

    /**
     * Constructor.
     *
     * @param openBankingOAuthSessionRepository the repository
     */
    @Autowired
    public OpenBankingOAuthInitializationIntegrationTest(
            OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository,
            OpenBankingOAuthRefreshTokenRepository openBankingOAuthRefreshTokenRepository,
            OpenBankingBankToBaseUriMapper openBankingBankToBaseUriMapper,
            OpenBankingOAuthAccessTokenRedisRepository openBankingOAuthAccessTokenRedisRepository,
            OpenBankingOAuthAccessTokenDetermineService openBankingOAuthAccessTokenDetermineService) {
        this.openBankingOAuthSessionRepository = openBankingOAuthSessionRepository;
        this.openBankingOAuthRefreshTokenRepository = openBankingOAuthRefreshTokenRepository;
        this.openBankingBankToBaseUriMapper = openBankingBankToBaseUriMapper;
        this.openBankingOAuthAccessTokenRedisRepository = openBankingOAuthAccessTokenRedisRepository;
        this.openBankingOAuthAccessTokenDetermineService = openBankingOAuthAccessTokenDetermineService;
    }

    /**
     * Clean up the database before each test.
     */
    @BeforeEach
    public void beforeEach() {
        openBankingOAuthSessionRepository.deleteAll();
        openBankingOAuthRefreshTokenRepository.deleteAll();
        openBankingOAuthAccessTokenRedisRepository.deleteAll();
        userRepository.deleteAll();
        mockServerClient.reset();
    }

    /**
     * Test initializing the OpenBankingOAuthSession and retrieving OpenBankingOAuthAccessToken.
     */
    @Test
    public void testOpenBankingOAuthInitializationHappyFlow() throws IOException {
        AllcountUser user = createUserForTesting();
        String jwtToken = getJwtToken(user);

        initializeOpenBankingOAuthSession(jwtToken, user, FILE_ACCESS_TOKEN_MOCK_RESPONSE_SUCCESSFUL);

        Assertions.assertNotNull(openBankingOAuthAccessTokenDetermineService.determineAccessToken(user,
                OpenBankingBankEnum.DEUTSCHE_BANK));
        Assertions.assertTrue(openBankingOAuthAccessTokenRedisRepository.existsById(
                OpenBankingOAuthAccessTokenRedisEntity.generateRedisId(OpenBankingBankEnum.DEUTSCHE_BANK, user.getId()))
        );
        Assertions.assertEquals(1, openBankingOAuthRefreshTokenRepository.count());
    }

    /**
     * Test initializing the OpenBankingOAuthSession and retrieving OpenBankingOAuthAccessToken after the token has expired.
     */
    @Test
    public void testOpenBankingOAuthInitializationTokenExpiry() throws IOException, InterruptedException {
        AllcountUser user = createUserForTesting();
        String jwtToken = getJwtToken(user);

        initializeOpenBankingOAuthSession(jwtToken, user, FILE_ACCESS_TOKEN_MOCK_RESPONSE_EXPIRY_INSTANT);

        TimeUnit.SECONDS.sleep(2);

        Assertions.assertFalse(openBankingOAuthAccessTokenRedisRepository.existsById(
                OpenBankingOAuthAccessTokenRedisEntity.generateRedisId(OpenBankingBankEnum.DEUTSCHE_BANK, user.getId()))
        );

        String mockResponse = getMockResponse(FILE_ACCESS_TOKEN_MOCK_RESPONSE_SUCCESSFUL);
        mockServerClient
                .when(request()
                        .withMethod(String.valueOf(HttpMethod.POST))
                        .withPath(ENDPOINT_TOKEN))
                .respond(response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withBody(mockResponse));

        Assertions.assertNotNull(openBankingOAuthAccessTokenDetermineService.determineAccessToken(user,
                OpenBankingBankEnum.DEUTSCHE_BANK));
        Assertions.assertTrue(openBankingOAuthAccessTokenRedisRepository.existsById(
                OpenBankingOAuthAccessTokenRedisEntity.generateRedisId(OpenBankingBankEnum.DEUTSCHE_BANK, user.getId()))
        );
        Assertions.assertEquals(2, openBankingOAuthRefreshTokenRepository.count());
    }

    private void initializeOpenBankingOAuthSession(
            String jwtToken,
            AllcountUser user,
            String scenarioMockResponsePath
    ) throws IOException {
        String sessionId = IntegrationTestLib.createAuthenticatedRequest(jwtToken)
                .queryParam(PARAM_BANK, OpenBankingBankEnum.DEUTSCHE_BANK)
                .get(ENDPOINT_USER_CREATE, user.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat()
                .body(FIELD_USER_ID, equalTo(user.getId().toString()))
                .body(FIELD_STATUS, equalTo(OpenBankingOAuthSessionStatusEnum.OAUTH_URI_GENERATED.name()))
                .extract()
                .path(FIELD_ID);

        OpenBankingSessionEntity session = openBankingOAuthSessionRepository
                .findById(UUID.fromString(sessionId))
                .orElseThrow();

        Mockito.when(openBankingBankToBaseUriMapper.getBaseUri(OpenBankingBankEnum.DEUTSCHE_BANK))
                .thenReturn(mockServerContainer.getEndpoint());

        String mockResponse = getMockResponse(scenarioMockResponsePath);
        mockServerClient
                .when(request()
                        .withMethod(String.valueOf(HttpMethod.POST))
                        .withPath(ENDPOINT_TOKEN))
                .respond(response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withBody(mockResponse));

        IntegrationTestLib.createAuthenticatedRequest(jwtToken)
                .queryParam(PARAM_CODE, TEST_CODE)
                .queryParam(PARAM_STATE, session.getState())
                .get(ENDPOINT_RETRIEVE_ACCESS_TOKEN)
                .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat()
                .body(FIELD_USER_ID, equalTo(user.getId().toString()));

        OpenBankingSessionEntity updatedSession = openBankingOAuthSessionRepository
                .findById(UUID.fromString(sessionId))
                .orElseThrow();

        Assertions.assertTrue(openBankingOAuthRefreshTokenRepository.existsByUserIdAndBank(
                user.getId(), OpenBankingBankEnum.DEUTSCHE_BANK));
        MatcherAssert.assertThat(updatedSession.getStatus(),
                equalTo(OpenBankingOAuthSessionStatusEnum.ACCESS_TOKEN_RECEIVED));
    }
}
