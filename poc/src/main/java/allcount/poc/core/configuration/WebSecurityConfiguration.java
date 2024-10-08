package allcount.poc.core.configuration;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration for the web security.
 */
@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER)
@EnableWebSecurity
public class WebSecurityConfiguration {

    /**
     * Bean for the security filter chain.
     *
     * @param http the http security.
     * @return the security filter chain.
     * @throws Exception if an error occurs.
     */
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authz) -> authz
                        .anyRequest().permitAll())
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.getOrBuild();
    }
}