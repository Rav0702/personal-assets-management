package allcount.poc.authentication.provider;

import java.time.Instant;
import org.springframework.stereotype.Component;


/**
 * Provides the current time.
 */
@Component
public class TimeProvider {

    /**
     * Instantiates a new Time provider.
     */
    public TimeProvider() {
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
