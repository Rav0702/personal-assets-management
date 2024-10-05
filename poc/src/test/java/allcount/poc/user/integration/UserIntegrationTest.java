package allcount.poc.user.integration;

import allcount.poc.core.test.integration.IntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class UserIntegrationTest extends IntegrationTest {

    private static final String ENDPOINT_USER_CREATE = "/v1/user/add";

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    public void testCreateUser() {
        given().when().post(ENDPOINT_USER_CREATE).then().statusCode(200);
    }
}
