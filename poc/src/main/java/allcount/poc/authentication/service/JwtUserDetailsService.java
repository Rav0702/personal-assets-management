package allcount.poc.authentication.service;

import allcount.poc.user.repository.AllcountUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service for user details.
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final transient AllcountUserRepository userRepository;

    /**
     * Instantiates a new JwtUserDetailsService.
     *
     * @param userRepository the user credential repository
     */
    @Autowired
    public JwtUserDetailsService(AllcountUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Load user by username.
     *
     * @param username the username
     * @return the user details
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow();
    }
}
