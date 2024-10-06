package allcount.poc.open_banking_authorization.integration;

import allcount.poc.core.test.integration.IntegrationTest;
import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Integration test for the user.
 */
public class OBIntegrationTest extends IntegrationTest {

    private static final String ENDPOINT_INIT = "v1/open-banking-authorization/initialize";

    /**
     * Clean up the database after each test.
     */
    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
    }

    /**
     * Test creating a user.
     */
    @Test
    public void testOBInit() {
        given().when().post(ENDPOINT_INIT).then().statusCode(200);
    }
}
