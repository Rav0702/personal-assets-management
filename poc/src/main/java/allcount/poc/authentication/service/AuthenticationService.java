package allcount.poc.authentication.service;

import allcount.poc.authentication.object.dto.AuthenticationRequestDto;
import allcount.poc.authentication.object.dto.AuthenticationResponseDto;
import allcount.poc.authentication.provider.JwtTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Service for authentication.
 */
@Service
public class AuthenticationService {

    private final transient AuthenticationManager authenticationManager;
    private final transient JwtUserDetailsService jwtUserDetailsService;
    private final transient JwtTokenGenerator jwtTokenGenerator;

    /**
     * Instantiates a new Authentication service.
     *
     * @param authenticationManager the authentication manager
     * @param jwtUserDetailsService the jwt user details service
     * @param jwtTokenUtil          the jwt token generator
     */
    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager,
                                 JwtUserDetailsService jwtUserDetailsService,
                                 JwtTokenGenerator jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenGenerator = jwtTokenUtil;
    }

    /**
     * Authenticate user.
     *
     * @param request the request
     * @return the authentication response model
     * @throws AuthenticationException the authentication exception
     */
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(request.getUsername());
        final String jwtToken = jwtTokenGenerator.generateToken(userDetails);

        return new AuthenticationResponseDto(jwtToken);
    }
}
