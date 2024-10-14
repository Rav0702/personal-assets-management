package allcount.poc.user.service;

import allcount.poc.user.object.HashedPassword;
import allcount.poc.user.object.Password;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Service for hashing passwords.
 */

public class HashedPasswordService {

    private final transient PasswordEncoder encoder;

    /**
     * Constructor.
     *
     * @param encoder the encoder for the password.
     */
    public HashedPasswordService(final PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    /**
     * Hashes a password.
     *
     * @param password the password to hash.
     * @return the hashed password.
     */
    public HashedPassword hash(final Password password) {
        return new HashedPassword(encoder.encode(password.toString()));
    }
}
