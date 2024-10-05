package allcount.poc.credential.object;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class HashedPassword {
    private final transient String hash;

    /**
     * Default constructor.
     *
     * @param hash hash
     */
    public HashedPassword(final String hash) {
        // Validate input
        this.hash = hash;
    }

    /**
     *
     * @return hash
     */
    @Override
    public String toString() {
        return hash;
    }
}
