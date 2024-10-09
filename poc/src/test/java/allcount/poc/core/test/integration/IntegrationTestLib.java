package allcount.poc.core.test.integration;

import static io.restassured.RestAssured.given;
import io.restassured.specification.RequestSpecification;

/**
 * Library for integration tests.
 */
public final class IntegrationTestLib {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    /**
     * Create authenticated request specification.
     *
     * @param token the token
     * @return the request specification
     */
    public static RequestSpecification createAuthenticatedRequest(
            final String token) {
        return given().header(AUTHORIZATION_HEADER,
                BEARER_TOKEN_PREFIX + token);
    }

}
