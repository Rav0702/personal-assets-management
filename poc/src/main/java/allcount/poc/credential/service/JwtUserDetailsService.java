package allcount.poc.credential.service;

import allcount.poc.credential.entity.UserCredential;
import allcount.poc.credential.repository.UserCredentialRepository;
import allcount.poc.user.entity.AllcountUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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
     * Load user by username.
     *
     * @param username the username
     * @return the user details
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredential userCredential = userCredentialRepository.findByUsername(username).orElseThrow();
        AllcountUser user = userCredential.getUser();

        return new User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }
}
