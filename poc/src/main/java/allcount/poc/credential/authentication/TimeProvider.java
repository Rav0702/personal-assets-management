package allcount.poc.credential.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Provides the current time.
 */
@Component
public class TimeProvider {

    /**
     * Instantiates a new Time provider.
     */
    public TimeProvider() {
        super();
    }

    /**
     * Get the current time.
     *
     * @return The current time
     */
    public Instant getCurrentTime() {
        return Instant.now();
    }
}
