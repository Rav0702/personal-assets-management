package allcount.poc.openbankingoauth.integration;

import allcount.poc.core.test.integration.IntegrationTest;
import allcount.poc.openbankingoauth.object.OpenBankingBankEnum;
import allcount.poc.user.entity.AllcountUser;
import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.Test;

/**
 * Integration test for the user.
 */
public class OpenBankingAuthorizationIntegrationTest extends IntegrationTest {
    private static final String ENDPOINT_USER_CREATE = "v1/open-banking-authorization/{userId}/initialize-session";
    private static final String PARAM_BANK = "bank";

    @Test
    public void testOpenBankingOAuthSessionInitialization() {
        AllcountUser user = createUserForTesting();

        given().queryParam("bank", OpenBankingBankEnum.DEUTSCHE_BANK)
                .get(ENDPOINT_USER_CREATE, user.getId())
                .then()
                .statusCode(200);
    }
}
