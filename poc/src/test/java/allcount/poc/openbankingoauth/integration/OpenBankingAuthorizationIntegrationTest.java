package allcount.poc.openbankingoauth.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import allcount.poc.core.test.integration.IntegrationTest;
import allcount.poc.openbankingoauth.entity.OpenBankingOAuthSessionEntity;
import allcount.poc.openbankingoauth.mapper.OpenBankingBankToBaseUriMapper;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.object.enums.OpenBankingOAuthSessionStatusEnum;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthAccessTokenRepository;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthSessionRepository;
import allcount.poc.user.entity.AllcountUser;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration test for initializing the OpenBankingOAuthSession.
 */
@ActiveProfiles("open-banking-authorization-integration-test")
public class OpenBankingAuthorizationIntegrationTest extends IntegrationTest {
    private static final String ENDPOINT_USER_CREATE = "v1/open-banking-authorization/{userId}/initialize-session";
    private static final String ENDPOINT_RETRIEVE_ACCESS_TOKEN = "v1/open-banking-authorization/retrieve-access-token";
    private static final String ENDPOINT_TOKEN = "/gw/oidc/token";

    private static final String PARAM_BANK = "bank";
    private static final String PARAM_CODE = "code";
    private static final String PARAM_STATE = "state";

    private static final String FIELD_STATUS = "status";

    private static final String TEST_CODE = "testCode";

    private static final String USER_DIR = "user.dir";
    private static final String PATH_RELATIVE =
            String.join(File.separator,
                    List.of("src", "test", "java", "allcount", "poc", "openbankingoauth", "integration"));
    private static final String FILE_ACCESS_TOKEN_MOCK_RESPONSE =
            PATH_RELATIVE + File.separator + "response" + File.separator + "accessTokenMockResponse.json";

    private final transient OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository;
    private final transient OpenBankingOAuthAccessTokenRepository openBankingOAuthAccessTokenRepository;
    private final transient OpenBankingBankToBaseUriMapper openBankingBankToBaseUriMapper;

    /**
     * Constructor.
     *
     * @param openBankingOAuthSessionRepository the repository
     */
    @Autowired
    public OpenBankingAuthorizationIntegrationTest(OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository,
                                                   OpenBankingOAuthAccessTokenRepository openBankingOAuthAccessTokenRepository,
                                                   OpenBankingBankToBaseUriMapper openBankingBankToBaseUriMapper) {
        this.openBankingOAuthSessionRepository = openBankingOAuthSessionRepository;
        this.openBankingOAuthAccessTokenRepository = openBankingOAuthAccessTokenRepository;
        this.openBankingBankToBaseUriMapper = openBankingBankToBaseUriMapper;
    }

    private static String getMockResponse(String path) throws IOException {
        return FileUtils.readFileToString(
                new File(System.getProperty(USER_DIR) + File.separator + path),
                StandardCharsets.UTF_8);
    }

    /**
     * Clean up the database after each test.
     */
    @AfterEach
    public void afterEach() {
        openBankingOAuthSessionRepository.deleteAll();
        openBankingOAuthAccessTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    /**
     * Test initializing the OpenBankingOAuthSession and retrieving OpenBankingOAuthAccessToken.
     */
    @Test
    public void testOpenBankingOAuthAuthorizationHappyFlow() throws IOException {
        AllcountUser user = createUserForTesting();

        String sessionId = given().queryParam(PARAM_BANK, OpenBankingBankEnum.DEUTSCHE_BANK)
                .get(ENDPOINT_USER_CREATE, user.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat()
                .body(FIELD_USER_ID, equalTo(user.getId().toString()))
                .body(FIELD_STATUS, equalTo(OpenBankingOAuthSessionStatusEnum.OAUTH_URI_GENERATED.name()))
                .extract()
                .path(FIELD_ID);

        OpenBankingOAuthSessionEntity session = openBankingOAuthSessionRepository
                .findById(UUID.fromString(sessionId))
                .orElseThrow();

        Mockito.when(openBankingBankToBaseUriMapper.getBaseUri(OpenBankingBankEnum.DEUTSCHE_BANK))
                .thenReturn(mockServerContainer.getEndpoint());

        String mockResponse = getMockResponse(FILE_ACCESS_TOKEN_MOCK_RESPONSE);
        mockServerClient
                .when(request()
                        .withMethod(String.valueOf(HttpMethod.POST))
                        .withPath(ENDPOINT_TOKEN))
                .respond(response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withBody(mockResponse));

        String accessTokenId = given().queryParam(PARAM_CODE, TEST_CODE)
                .queryParam(PARAM_STATE, session.getState())
                .get(ENDPOINT_RETRIEVE_ACCESS_TOKEN)
                .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat()
                .body(FIELD_USER_ID, equalTo(user.getId().toString()))
                .extract()
                .path(FIELD_ID);

        OpenBankingOAuthSessionEntity updatedSession = openBankingOAuthSessionRepository
                .findById(UUID.fromString(sessionId))
                .orElseThrow();

        Assertions.assertTrue(openBankingOAuthAccessTokenRepository.existsById(UUID.fromString(accessTokenId)));
        MatcherAssert.assertThat(updatedSession.getStatus(),
                equalTo(OpenBankingOAuthSessionStatusEnum.ACCESS_TOKEN_RECEIVED));
    }
}
