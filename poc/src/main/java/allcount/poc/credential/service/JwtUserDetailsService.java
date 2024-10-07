package allcount.poc.credential.service;

import allcount.poc.credential.entity.UserCredential;
import allcount.poc.credential.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *  Service for user details.
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final transient UserCredentialRepository userCredentialRepository;

    /**
     * Instantiates a new JwtUserDetailsService.
     *
     * @param userCredentialRepository the user credential repository
     */
    @Autowired
    public JwtUserDetailsService(UserCredentialRepository userCredentialRepository) {
        this.userCredentialRepository = userCredentialRepository;
    }

    /**
     * Loads user information required for authentication from the DB.
     *
     * @param username The username of the user we want to authenticate
     * @return The authentication user information of that user
     * @throws UsernameNotFoundException Username was not found
     */
    public UserCredential loadUserByUsername(String username) throws UsernameNotFoundException {
        var optionalUser = userCredentialRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User does not exist");
        }

        return optionalUser.get();
    }
}
