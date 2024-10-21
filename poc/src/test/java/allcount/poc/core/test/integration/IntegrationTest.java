package allcount.poc.core.test.integration;

import static io.restassured.RestAssured.given;
import allcount.poc.authentication.object.dto.AuthenticationRequestDto;
import allcount.poc.authentication.object.dto.RegistrationRequestDto;
import allcount.poc.core.configuration.TestcontainersConfiguration;
import allcount.poc.user.entity.AllcountUser;
import allcount.poc.user.repository.AllcountUserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockserver.client.MockServerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Base class for integration tests.
 */
@Testcontainers
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class IntegrationTest {
    protected static final String BASE_URI = "http://localhost:";
    protected static final String FIELD_ID = "id";
    protected static final String FIELD_USER_ID = "userId";
    protected static final String ENDPOINT_REGISTER = "/v1/auth/register";
    protected static final String ENDPOINT_AUTHENTICATE = "/v1/auth/authenticate";
    protected static final String FIELD_TOKEN = "token";
    protected static final String SUFFIX_EMAIL_DOMAIN = "@example.com";
    private static final String USER_DIR = "user.dir";
    @Autowired
    protected transient AllcountUserRepository userRepository;

    protected transient MockServerContainer mockServerContainer = TestcontainersConfiguration.getMockServerContainer();
    protected transient MockServerClient mockServerClient = TestcontainersConfiguration.getMockServerClient();

    @LocalServerPort
    protected int port;

    protected String testPassword;

    /**
     * Get the mock response.
     *
     * @param path the path
     * @return the mock response
     * @throws IOException if there is an issue reading the file
     */
    protected static String getMockResponse(String path) throws IOException {
        return FileUtils.readFileToString(
                new File(System.getProperty(USER_DIR) + File.separator + path),
                StandardCharsets.UTF_8);
    }

    /**
     * Set up the base URI.
     */
    @BeforeEach
    protected void setupBaseUri() {
        RestAssured.baseURI = BASE_URI + port;
        testPassword = UUID.randomUUID().toString();
    }

    /**
     * Create a user for testing.
     *
     * @return the user
     */
    protected AllcountUser createUserForTesting() {
        RegistrationRequestDto registrationRequestDto = getRegistrationRequestModel();

        String userId = given()
                .contentType(ContentType.JSON)
                .body(registrationRequestDto)
                .when()
                .post(ENDPOINT_REGISTER)
                .then()
                .statusCode(HttpStatus.OK.value()).extract().path(FIELD_USER_ID);

        AllcountUser user =
                userRepository.findByIdWithUserCredentialForTestingOnly(UUID.fromString(userId)).orElseThrow();
        Assertions.assertEquals(registrationRequestDto.getUsername(), user.getUsername());

        return user;
    }

    /**
     * Get the JWT token.
     *
     * @param user the user
     * @return the JWT token
     */
    protected String getJwtToken(AllcountUser user) {
        AuthenticationRequestDto authenticationRequest = new AuthenticationRequestDto();
        authenticationRequest.setUsername(user.getUsername());
        authenticationRequest.setPassword(testPassword);

        return given()
                .contentType(ContentType.JSON)
                .body(authenticationRequest)
                .when()
                .post(ENDPOINT_AUTHENTICATE)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().path(FIELD_TOKEN);
    }

    /**
     * Get the registration request model.
     *
     * @return the registration request model
     */
    private RegistrationRequestDto getRegistrationRequestModel() {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto();
        registrationRequestDto.setEmail(UUID.randomUUID() + SUFFIX_EMAIL_DOMAIN);
        registrationRequestDto.setPassword(testPassword);
        registrationRequestDto.setFirstName(UUID.randomUUID().toString());
        registrationRequestDto.setLastName(UUID.randomUUID().toString());
        registrationRequestDto.setUsername(UUID.randomUUID().toString());

        return registrationRequestDto;
    }
}
