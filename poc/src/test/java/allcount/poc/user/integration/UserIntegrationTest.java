package allcount.poc.user.integration;

import allcount.poc.core.test.integration.IntegrationTest;
import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Integration test for the user.
 */
public class UserIntegrationTest extends IntegrationTest {

    private static final String ENDPOINT_USER_CREATE = "/v1/user/add";

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
    public void testCreateUser() {
        given().when().post(ENDPOINT_USER_CREATE).then().statusCode(200);
    }
}
