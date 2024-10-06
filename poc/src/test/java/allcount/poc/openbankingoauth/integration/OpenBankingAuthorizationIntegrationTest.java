package allcount.poc.openbankingoauth.integration;

import allcount.poc.core.test.integration.IntegrationTest;
import allcount.poc.openbankingoauth.object.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthSessionRepository;
import allcount.poc.user.entity.AllcountUser;
import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Integration test for the user.
 */
public class OpenBankingAuthorizationIntegrationTest extends IntegrationTest {
    private static final String ENDPOINT_USER_CREATE = "v1/open-banking-authorization/{userId}/initialize-session";
    private static final String PARAM_BANK = "bank";


    private final transient OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository;

    /**
     * Constructor.
     *
     * @param openBankingOAuthSessionRepository the repository
     */
    @Autowired
    public OpenBankingAuthorizationIntegrationTest(OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository) {
        this.openBankingOAuthSessionRepository = openBankingOAuthSessionRepository;
    }

    /**
     * Clean up the database after each test.
     */
    @AfterEach
    public void afterEach() {
        openBankingOAuthSessionRepository.deleteAll();
        userRepository.deleteAll();
    }

    /**
     * Test creating a user.
     */
    @Test
    public void testOpenBankingOAuthSessionInitialization() {
        AllcountUser user = createUserForTesting();

        given().queryParam(PARAM_BANK, OpenBankingBankEnum.DEUTSCHE_BANK)
                .get(ENDPOINT_USER_CREATE, user.getId())
                .then()
                .statusCode(200);
    }
}
