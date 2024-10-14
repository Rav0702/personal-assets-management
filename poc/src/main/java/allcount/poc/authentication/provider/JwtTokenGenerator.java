package allcount.poc.authentication.provider;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Generates JWT tokens.
 */
@Component
public class JwtTokenGenerator {
    public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 1000;
    private final transient TimeProvider timeProvider;
    @Value("${jwt.secret}")
    private transient String jwtSecret;

    /**
     * Instantiates a new Jwt token generator.
     *
     * @param timeProvider the time provider
     */
    @Autowired
    public JwtTokenGenerator(final TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    /**
     * Generate token string.
     *
     * @param userDetails the user details
     * @return the string
     */
    public String generateToken(final UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(timeProvider.getCurrentTime().toEpochMilli()))
                .expiration(new Date(timeProvider.getCurrentTime().toEpochMilli() + JWT_TOKEN_VALIDITY))
                .signWith(key)
                .compact();
    }
}
