package allcount.poc.credential.authentication;

import allcount.poc.credential.service.HashedPasswordService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * The type Security config.
 */
@Getter
@Setter
@Configuration
public class SecurityConfig {

    private transient UserDetailsService userDetailsService;

    /**
     * Bean for password encoder implementation.
     *
     * @return the password encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean for password hashing service.
     *
     * @return password hashing service
     */
    @Bean
    public HashedPasswordService passwordHashEncoder() {
        return new HashedPasswordService(passwordEncoder());
    }

    /**
     * Bean for authentication manager.
     *
     * @param userDetailsService user details service
     * @param passwordEncoder password encoder
     * @return authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(
            final UserDetailsService userDetailsService,
            final PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }
}
