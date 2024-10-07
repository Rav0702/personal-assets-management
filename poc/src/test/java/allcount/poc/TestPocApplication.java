package allcount.poc;

import allcount.poc.core.configuration.TestcontainersConfiguration;
import org.springframework.boot.SpringApplication;

/**
 * Main class for testcontainers.
 */
public class TestPocApplication {

    /**
     * Main method.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        SpringApplication.from(PocApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
