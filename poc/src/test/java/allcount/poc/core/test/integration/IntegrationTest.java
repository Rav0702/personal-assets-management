package allcount.poc.core.test.integration;

import allcount.poc.core.configuration.TestcontainersConfiguration;
import allcount.poc.user.entity.AllcountUser;
import allcount.poc.user.repository.AllcountUserRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Base class for integration tests.
 */
@Testcontainers
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class IntegrationTest {
    protected static final String BASE_URI = "http://localhost:";

    @Autowired
    protected transient AllcountUserRepository userRepository;


    @LocalServerPort
    protected int port;

    /**
     * Set up the base URI.
     */
    @BeforeEach
    protected void setupBaseUri() {
        RestAssured.baseURI = BASE_URI + port;
    }

    /**
     * Create a user for testing.
     *
     * @return the user
     */
    protected AllcountUser createUserForTesting() {
        AllcountUser user = AllcountUser.builder().build();

        return userRepository.save(user);
    }
}
