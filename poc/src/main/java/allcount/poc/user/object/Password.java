package allcount.poc.user.object;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 * Object for the password.
 */
@EqualsAndHashCode
@AllArgsConstructor
public class Password {
    private final transient String passwordValue;

    /**
     * String representation of the object.
     *
     * @return String representation of the object
     */
    @Override
    public String toString() {
        return passwordValue;
    }
}
