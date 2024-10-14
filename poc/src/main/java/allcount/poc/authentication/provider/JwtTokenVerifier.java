package allcount.poc.authentication.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Verifies the JWT token.
 */
@Component
public class JwtTokenVerifier {

    @Value("${jwt.secret}")
    private transient String jwtSecret;

    /**
     * Validate the JWT token for expiration.
     *
     * @param token the JWT token
     * @return true if the token is valid
     */
    public boolean validateToken(final String token) {
        return !isTokenExpired(token);
    }

    /**
     * Get the user id from the JWT token.
     *
     * @param token the JWT token
     * @return the user id
     */
    public String getUsernameFromToken(final String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Get the expiration date from the JWT token.
     *
     * @param token the JWT token
     * @return the expiration date
     */
    public Date getExpirationDateFromToken(final String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Check if the JWT token is expired.
     *
     * @param token the JWT token
     * @return true if the token is expired
     */
    private Boolean isTokenExpired(final String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Get the claim from the JWT token.
     *
     * @param token          the JWT token
     * @param claimsResolver the claims resolver
     * @param <T>            the type of the claim
     * @return the claim
     */
    private <T> T getClaimFromToken(final String token,
                                    final Function<Claims, T> claimsResolver) {
        final Claims claims = getClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Get the claims from the JWT token.
     *
     * @param token the JWT token
     * @return the claims
     */
    private Claims getClaims(final String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
