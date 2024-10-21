package allcount.poc.cashaccount.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import allcount.poc.cashaccount.entity.CashAccountEntity;
import allcount.poc.cashaccount.repository.CashAccountRepository;
import allcount.poc.cashaccount.service.CashAccountService;
import allcount.poc.core.test.integration.IntegrationTest;
import allcount.poc.openbankingoauth.entity.OpenBankingOAuthAccessTokenRedisEntity;
import allcount.poc.openbankingoauth.entity.OpenBankingSessionEntity;
import allcount.poc.openbankingoauth.mapper.OpenBankingBankToBaseUriMapper;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.object.enums.OpenBankingOAuthAccessTokenTypeEnum;
import allcount.poc.openbankingoauth.object.enums.OpenBankingOAuthSessionStatusEnum;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthAccessTokenRedisRepository;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthSessionRepository;
import allcount.poc.user.entity.AllcountUser;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
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
public class CashAccountIntegrationTest extends IntegrationTest {
    private static final String PATH_RELATIVE =
            String.join(File.separator,
                    List.of("src", "test", "java", "allcount", "poc", "cashaccount", "integration"));
    private static final String FILE_CASH_ACCOUNT_LISTING =
            PATH_RELATIVE + File.separator + "response" + File.separator + "cashAccountListingMockResponse.json";

    private final transient CashAccountService cashAccountService;
    private final transient OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository;
    private final transient OpenBankingOAuthAccessTokenRedisRepository openBankingOAuthAccessTokenRedisRepository;
    private final transient OpenBankingBankToBaseUriMapper openBankingBankToBaseUriMapper;
    private final transient CashAccountRepository cashAccountRepository;

    /**
     * Constructor.
     *
     * @param cashAccountService the service
     * @param openBankingOAuthSessionRepository the repository
     * @param openBankingOAuthAccessTokenRedisRepository the repository
     * @param openBankingBankToBaseUriMapper the mapper
     * @param cashAccountRepository the repository
     */
    @Autowired
    public CashAccountIntegrationTest(CashAccountService cashAccountService,
                                      OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository,
                                      OpenBankingOAuthAccessTokenRedisRepository openBankingOAuthAccessTokenRedisRepository,
                                      OpenBankingBankToBaseUriMapper openBankingBankToBaseUriMapper,
                                      CashAccountRepository cashAccountRepository) {
        this.cashAccountService = cashAccountService;
        this.openBankingOAuthSessionRepository = openBankingOAuthSessionRepository;
        this.openBankingOAuthAccessTokenRedisRepository = openBankingOAuthAccessTokenRedisRepository;
        this.openBankingBankToBaseUriMapper = openBankingBankToBaseUriMapper;
        this.cashAccountRepository = cashAccountRepository;
    }

    /**
     * Clean up the database before each test.
     */
    @BeforeEach
    public void beforeEach() {
        cleanUp();
    }

    /**
     * Clean up the database after each test.
     */
    @AfterEach
    public void afterEach() {
        cleanUp();
    }

    /**
     * Clean up the database.
     */
    private void cleanUp() {
        cashAccountRepository.deleteAll();
        openBankingOAuthAccessTokenRedisRepository.deleteAll();
        openBankingOAuthSessionRepository.deleteAll();
        userRepository.deleteAll();
        mockServerClient.reset();
    }

    /**
     * Test retrieving the cash accounts.
     */
    @Test
    public void testCashAccountRetrieve() throws IOException {
        AllcountUser user = createUserForTesting();
        OpenBankingSessionEntity openBankingSession = OpenBankingSessionEntity.builder()
                .user(user)
                .bank(OpenBankingBankEnum.DEUTSCHE_BANK)
                .status(OpenBankingOAuthSessionStatusEnum.ACCESS_TOKEN_RECEIVED)
                .state(UUID.randomUUID())
                .codeVerifier(UUID.randomUUID().toString())
                .redirectLoginUri(UUID.randomUUID().toString())
                .build();
        openBankingOAuthSessionRepository.save(openBankingSession);
        OpenBankingOAuthAccessTokenRedisEntity openBankingOAuthAccessToken = new OpenBankingOAuthAccessTokenRedisEntity(
                UUID.randomUUID().toString(),
                OpenBankingOAuthAccessTokenTypeEnum.BEARER,
                user.getId(),
                OpenBankingBankEnum.DEUTSCHE_BANK,
                3600L
        );
        openBankingOAuthAccessTokenRedisRepository.save(openBankingOAuthAccessToken);

        Mockito.when(openBankingBankToBaseUriMapper.getBaseUri(OpenBankingBankEnum.DEUTSCHE_BANK))
                .thenReturn(mockServerContainer.getEndpoint());

        String mockResponse = getMockResponse(FILE_CASH_ACCOUNT_LISTING);
        mockServerClient
                .when(request()
                        .withMethod(String.valueOf(HttpMethod.GET)))
                .respond(response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withBody(mockResponse));

        cashAccountService.retrieveAccounts(user.getId());

        List<CashAccountEntity> cashAccountEntities = cashAccountRepository.findAll();

        MatcherAssert.assertThat(cashAccountEntities.size(), equalTo(1));
    }
}
