package allcount.poc.credential.service;

import allcount.poc.credential.object.HashedPassword;
import allcount.poc.credential.object.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for hashing passwords.
 */
@Service
public class HashedPasswordService {

    private final transient BCryptPasswordEncoder encoder;

    /**
     * Constructor.
     *
     * @param encoder the encoder for the password.
     */
    @Autowired
    public HashedPasswordService(final BCryptPasswordEncoder encoder) {
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
