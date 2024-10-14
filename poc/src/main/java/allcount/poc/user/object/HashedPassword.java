package allcount.poc.user.object;

import lombok.EqualsAndHashCode;

/**
 * Object for the hashed password.
 */
@EqualsAndHashCode
public class HashedPassword {
    private final transient String hash;

    /**
     * Default constructor.
     *
     * @param hash hash
     */
    public HashedPassword(final String hash) {
        this.hash = hash;
    }

    /**
     * String representation of the object.
     *
     * @return String representation of the object
     */
    @Override
    public String toString() {
        return hash;
    }
}
