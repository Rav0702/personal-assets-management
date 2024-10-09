package allcount.poc.authentication.controller;

import allcount.poc.authentication.object.dto.AuthenticationRequestDto;
import allcount.poc.authentication.object.dto.AuthenticationResponseDto;
import allcount.poc.authentication.object.dto.RegistrationRequestDto;
import allcount.poc.authentication.object.dto.RegistrationResponseDto;
import allcount.poc.authentication.service.AuthenticationService;
import allcount.poc.authentication.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for authentication.
 */
@RestController
@RequestMapping(path = "v1/auth")
public class AuthenticationController {

    private final transient AuthenticationService authenticationService;
    private final transient RegistrationService registrationService;

    /**
     * Instantiates a new AuthenticationController.
     *
     * @param authenticationService the authentication service
     * @param registrationService   the registration service
     */
    @Autowired
    public AuthenticationController(
            AuthenticationService authenticationService,
            RegistrationService registrationService
    ) {
        this.authenticationService = authenticationService;
        this.registrationService = registrationService;
    }

    /**
     * Endpoint for authentication.
     *
     * @param request The login model
     * @return JWT token if the login is successful
     */
    @PostMapping("/authenticate")
    public AuthenticationResponseDto authenticate(@RequestBody AuthenticationRequestDto request) {
        return authenticationService.authenticate(request);
    }

    /**
     * Endpoint for registration.
     *
     * @param registrationRequest The registration model
     * @return the user id of the registered user
     */
    @PostMapping("/register")
    public RegistrationResponseDto register(@RequestBody RegistrationRequestDto registrationRequest) {
        return registrationService.registerUser(registrationRequest);
    }
}

