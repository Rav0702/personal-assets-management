package allcount.poc.core.configuration;

import org.mockserver.client.MockServerClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

/**
 * Configuration for the testcontainers.
 */
@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    @Container
    static MockServerContainer mockServerContainer =
            new MockServerContainer(DockerImageName.parse("mockserver/mockserver:5.15.0"));
    static MockServerClient mockServerClient;

    /**
     * Bean for the MySQL container.
     *
     * @return the MySQL container.
     */
    @Bean
    @ServiceConnection
    MySQLContainer<?> mysqlContainer() {
        return new MySQLContainer<>(DockerImageName.parse("mysql:latest"));
    }

    /**
     * Gets the mock server container.
     *
     * @return the mock server container.
     */
    public static MockServerContainer getMockServerContainer() {
        startMockServerContainerIfNeeded();

        return mockServerContainer;
    }

    /**
     * Starts the mock server container if needed.
     */
    private static void startMockServerContainerIfNeeded() {
        if (!mockServerContainer.isRunning()) {
            mockServerContainer.start();
        }
    }

    /**
     * Gets the mock server client.
     *
     * @return the mock server client.
     */
    public static MockServerClient getMockServerClient() {
        startMockServerContainerIfNeeded();

        if (mockServerClient == null) {
            mockServerClient = new MockServerClient(mockServerContainer.getHost(), mockServerContainer.getServerPort());
        }

        return mockServerClient;
    }
}
