package allcount.poc.credential.controller;

import allcount.poc.credential.authentication.JwtTokenGenerator;
import allcount.poc.credential.entity.UserCredential;
import allcount.poc.credential.models.AuthenticationRequestModel;
import allcount.poc.credential.models.AuthenticationResponseModel;
import allcount.poc.credential.models.RegistrationRequestModel;
import allcount.poc.credential.models.RegistrationResponseModel;
import allcount.poc.credential.object.Password;
import allcount.poc.credential.service.JwtUserDetailsService;
import allcount.poc.user.service.RegistrationService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for authentication.
 */
@RestController
public class AuthenticationController {

    private final transient AuthenticationManager authenticationManager;

    private final transient JwtTokenGenerator jwtTokenGenerator;

    private final transient JwtUserDetailsService jwtUserDetailsService;

    private final transient RegistrationService registrationService;

    /**
     * Instantiates a new UsersController.
     *
     * @param authenticationManager the authentication manager
     * @param jwtTokenGenerator     the token generator
     * @param jwtUserDetailsService the user details service
     * @param registrationService   the registration service
     */
    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtTokenGenerator jwtTokenGenerator,
                                    JwtUserDetailsService jwtUserDetailsService,
                                    RegistrationService registrationService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.registrationService = registrationService;
    }

    /**
     * Endpoint for authentication.
     *
     * @param request The login model
     * @return JWT token if the login is successful
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseModel> authenticate(@RequestBody AuthenticationRequestModel request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()));

            final UserCredential userCredential = jwtUserDetailsService.loadUserByUsername(request.getUsername());
            final String jwtToken = jwtTokenGenerator.generateToken(userCredential);
            return ResponseEntity.ok(new AuthenticationResponseModel(jwtToken));

        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", e);
        }


    }

    /**
     * Endpoint for registration.
     *
     * @param request The registration model
     * @return the user id of the registered user
     */
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseModel> register(@RequestBody RegistrationRequestModel request) {
        try {
            Password password = new Password(request.getPassword());
            UUID registeredUserId = registrationService.registerUser(
                request.getEmail(),
                password,
                request.getUsername(),
                request.getFirstName(),
                request.getLastName()
            );
            RegistrationResponseModel response = new RegistrationResponseModel(registeredUserId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}

