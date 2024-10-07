package allcount.poc.user.service;

import allcount.poc.credential.entity.UserCredential;
import allcount.poc.credential.object.HashedPassword;
import allcount.poc.credential.object.Password;
import allcount.poc.credential.repository.UserCredentialRepository;
import allcount.poc.credential.service.HashedPasswordService;
import allcount.poc.user.entity.AllcountUser;
import allcount.poc.user.entity.AllcountUserDetailsEntity;
import allcount.poc.user.repository.AllcountUserRepository;
import allcount.poc.user.repository.UserDetailsRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * Service for user registration.
 */
@Service
public class RegistrationService {
    private final transient AllcountUserRepository allcountUserRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final UserDetailsRepository userDetailsRepository;

    private final transient HashedPasswordService hashedPasswordService;

    private static final String apiPrefix = "http://localhost:";
    private static final String userPath = "/api/user";

    @Autowired
    private transient RestTemplate restTemplate;

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
     * @param email    email
     * @param password password
     * @param userName username
     * @param firstName first name
     * @param lastName last name
     * @return the id of the new user
     * @throws Exception if the user could not be registered
     */
    public UUID registerUser(String email, Password password, String userName, String firstName, String lastName) throws Exception {
        try {
            if (checkDuplicateUsername(userName)) {
                throw new RuntimeException("Username " + userName + " is already taken");
            }



            // Create new account
            AllcountUser allcountUser = new AllcountUser();
            AllcountUserDetailsEntity allcountUserDetailsEntity = new AllcountUserDetailsEntity();
            UserCredential userCredential = new UserCredential();

            allcountUser.setUserDetails(allcountUserDetailsEntity);
            allcountUser.setUserCredential(userCredential);

            // save allCountUser
            allcountUserRepository.save(allcountUser);

            allcountUserDetailsEntity.setEmail(email);
            allcountUserDetailsEntity.setFirstName(firstName);
            allcountUserDetailsEntity.setLastName(lastName);

            // Hash password
            HashedPassword hashedPassword = hashedPasswordService.hash(password);
            // save allCountUserDetailsEntity
            userDetailsRepository.save(allcountUserDetailsEntity);

            userCredential.setUsername(userName);
            userCredential.setPassword(hashedPassword);

            // save userCredential
            userCredentialRepository.save(userCredential);

            return allcountUser.getId();
        } catch (Exception e) {
            throw new Exception("Could not register user", e);
        }
    }

    /**
     * Checks if the username is already taken.
     *
     * @param userName username
     * @return true if the username is not taken
     */
    public boolean checkDuplicateUsername(String userName) {
        return userDetailsRepository.findByUsername(userName).isEmpty();
    }
}
