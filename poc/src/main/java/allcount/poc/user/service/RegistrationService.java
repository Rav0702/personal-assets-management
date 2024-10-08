package allcount.poc.user.service;

import allcount.poc.credential.entity.UserCredential;
import allcount.poc.credential.models.RegistrationRequestModel;
import allcount.poc.credential.models.RegistrationResponseModel;
import allcount.poc.credential.object.HashedPassword;
import allcount.poc.credential.object.Password;
import allcount.poc.credential.repository.UserCredentialRepository;
import allcount.poc.credential.service.HashedPasswordService;
import allcount.poc.user.entity.AllcountUser;
import allcount.poc.user.entity.AllcountUserDetailsEntity;
import allcount.poc.user.repository.AllcountUserRepository;
import allcount.poc.user.repository.UserDetailsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service for user registration.
 */
@Service
public class RegistrationService {
    private final transient AllcountUserRepository allcountUserRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final UserDetailsRepository userDetailsRepository;

    private final transient HashedPasswordService hashedPasswordService;

    /**
     * Instantiates a new RegistrationService.
     *
     * @param allcountUserRepository the allcount user repository
     * @param hashedPasswordService  the hashed password service
     * @param userCredentialRepository the user credential repository
     * @param userDetailsRepository the user details repository
     */
    public RegistrationService(AllcountUserRepository allcountUserRepository,
                               HashedPasswordService hashedPasswordService,
                               UserCredentialRepository userCredentialRepository, UserDetailsRepository userDetailsRepository) {
        this.allcountUserRepository = allcountUserRepository;
        this.hashedPasswordService = hashedPasswordService;
        this.userCredentialRepository = userCredentialRepository;
        this.userDetailsRepository = userDetailsRepository;
    }

    /**
     * Registers a new user.
     *
     * @return the id of the new user
     */
    @Transactional
    public RegistrationResponseModel registerUser(RegistrationRequestModel registrationRequest) {
        AllcountUser user = new AllcountUser();
        user.setUserDetails(createAllcountUserDetails(user, registrationRequest));
        user.setUserCredential(createAllcountUserCredential(user, registrationRequest));

        allcountUserRepository.save(user);

        return new RegistrationResponseModel(user.getId());
    }

    /**
     * Creates a new user details.
     *
     * @param registrationRequest the registration request
     */
    private AllcountUserDetailsEntity createAllcountUserDetails(AllcountUser user, RegistrationRequestModel registrationRequest) {
        AllcountUserDetailsEntity allcountUserDetailsEntity = new AllcountUserDetailsEntity();
        allcountUserDetailsEntity.setEmail(registrationRequest.getEmail());
        allcountUserDetailsEntity.setFirstName(registrationRequest.getFirstName());
        allcountUserDetailsEntity.setLastName(registrationRequest.getLastName());
        allcountUserDetailsEntity.setUser(user);

        return userDetailsRepository.save(allcountUserDetailsEntity);
    }

    /**
     * Creates a new user credential.
     *
     * @param registrationRequest the registration request
     */
    private UserCredential createAllcountUserCredential(AllcountUser user, RegistrationRequestModel registrationRequest) {
        UserCredential userCredential = new UserCredential();
        HashedPassword hashedPassword = hashedPasswordService.hash(new Password(registrationRequest.getPassword()));
        userCredential.setUsername(registrationRequest.getUsername());
        userCredential.setPassword(hashedPassword);
        userCredential.setUser(user);

        return userCredential;
    }

    /**
     * Checks if the username is already taken.
     *
     * @param userName username
     * @return true if the username is not taken
     */
    public boolean checkDuplicateUsername(String userName) {
        return userCredentialRepository.findByUsername(userName).isEmpty();
    }
}
