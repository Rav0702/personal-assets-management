package allcount.poc.credential.controller;

import allcount.poc.credential.models.AuthenticationRequestModel;
import allcount.poc.credential.models.AuthenticationResponseModel;
import allcount.poc.credential.models.RegistrationRequestModel;
import allcount.poc.credential.models.RegistrationResponseModel;
import allcount.poc.credential.service.AuthenticationService;
import allcount.poc.user.service.RegistrationService;
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
     * @param registrationService the registration service
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
    public AuthenticationResponseModel authenticate(@RequestBody AuthenticationRequestModel request) {
        return authenticationService.authenticate(request);
    }

    /**
     * Endpoint for registration.
     *
     * @param registrationRequest The registration model
     * @return the user id of the registered user
     */
    @PostMapping("/register")
    public RegistrationResponseModel register(@RequestBody RegistrationRequestModel registrationRequest) {
        return registrationService.registerUser(registrationRequest);
    }
}

