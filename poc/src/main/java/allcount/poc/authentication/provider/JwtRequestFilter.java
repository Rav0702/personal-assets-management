package allcount.poc.authentication.provider;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

/**
 * The Jwt request filter.
 */
@Component
public class JwtRequestFilter implements Filter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_AUTH_SCHEME = "Bearer";
    public static final int DIRECTIVES_LENGTH_EXPECTED = 2;
    public static final int AUTHORIZATION_AUTH_SCHEME_DIRECTIVE_INDEX = 0;
    public static final int JWT_TOKEN_DIRECTIVE_INDEX = 1;
    public static final String SEPARATOR_SPACE = " ";

    private final transient JwtTokenVerifier jwtTokenVerifier;
    private final Logger log = Logger.getLogger(this.getClass().getName());

    /**
     * Instantiates a new Jwt request filter.
     *
     * @param jwtTokenVerifier the jwt token verifier
     */
    @Autowired
    public JwtRequestFilter(final JwtTokenVerifier jwtTokenVerifier) {
        this.jwtTokenVerifier = jwtTokenVerifier;
    }

    /**
     * Implementation of the doFilter method from Filter.
     *
     * @param request     request
     * @param response    response
     * @param filterChain filter chain
     * @throws IOException      exception
     * @throws ServletException exception
     */
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authorizationHeader = getAuthorizationHeader(httpRequest);

        if (authorizationHeader != null) {
            processAuthorizationHeader(httpRequest, authorizationHeader);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Gets the authorization header.
     *
     * @param httpRequest the HTTP request
     * @return the authorization header
     */
    private String getAuthorizationHeader(HttpServletRequest httpRequest) {
        return httpRequest.getHeader(AUTHORIZATION_HEADER);
    }

    private void processAuthorizationHeader(HttpServletRequest httpRequest, String authorizationHeader) {
        String[] directives = authorizationHeader.split(SEPARATOR_SPACE);

        if (isValidAuthScheme(directives)) {
            String token = directives[JWT_TOKEN_DIRECTIVE_INDEX];

            try {
                setAuthenticationContextIfValidToken(httpRequest, token);
            } catch (ExpiredJwtException e) {
                log.info("JWT token has expired.");
            } catch (IllegalArgumentException | JwtException e) {
                log.info("Invalid JWT token.");
            }
        }
    }

    /**
     * Checks if the authorization scheme is valid.
     *
     * @param directives the directives
     * @return true if the authorization scheme is valid
     */
    private boolean isValidAuthScheme(String[] directives) {
        return directives.length == DIRECTIVES_LENGTH_EXPECTED
                && directives[AUTHORIZATION_AUTH_SCHEME_DIRECTIVE_INDEX].equals(AUTHORIZATION_AUTH_SCHEME);
    }

    /**
     * Sets the authentication context if the token is valid.
     *
     * @param httpRequest the HTTP request
     * @param token       the token
     */
    private void setAuthenticationContextIfValidToken(HttpServletRequest httpRequest, String token) {
        if (jwtTokenVerifier.validateToken(token)) {
            String username = jwtTokenVerifier.getUsernameFromToken(token);
            var authenticationToken = new UsernamePasswordAuthenticationToken(username, null, List.of());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }
}
