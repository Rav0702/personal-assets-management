package allcount.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main class for the application.
 */
@EnableScheduling
@EnableTransactionManagement
@EnableRedisRepositories
@SpringBootApplication
public class PocApplication {
    /**
     * Main method.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(PocApplication.class, args);
    }
}
