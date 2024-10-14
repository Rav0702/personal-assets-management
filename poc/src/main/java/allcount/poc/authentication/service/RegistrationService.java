package allcount.poc.authentication.service;

import allcount.poc.authentication.object.dto.RegistrationRequestDto;
import allcount.poc.authentication.object.dto.RegistrationResponseDto;
import allcount.poc.user.entity.AllcountUser;
import allcount.poc.user.entity.AllcountUserDetailsEntity;
import allcount.poc.user.entity.UserCredential;
import allcount.poc.user.object.HashedPassword;
import allcount.poc.user.object.Password;
import allcount.poc.user.repository.AllcountUserRepository;
import allcount.poc.user.service.HashedPasswordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service for user registration.
 */
@Service
public class RegistrationService {
    private final transient AllcountUserRepository allcountUserRepository;

    private final transient HashedPasswordService hashedPasswordService;

    /**
     * Instantiates a new RegistrationService.
     *
     * @param allcountUserRepository the allcount user repository
     * @param hashedPasswordService  the hashed password service
     */
    public RegistrationService(
            AllcountUserRepository allcountUserRepository,
            HashedPasswordService hashedPasswordService
    ) {
        this.allcountUserRepository = allcountUserRepository;
        this.hashedPasswordService = hashedPasswordService;
    }

    /**
     * Registers a new user.
     *
     * @return the id of the new user
     */
    @Transactional
    public RegistrationResponseDto registerUser(RegistrationRequestDto registrationRequest) {
        AllcountUser user = new AllcountUser();
        user.setUserDetails(createAllcountUserDetails(user, registrationRequest));
        user.setUserCredential(createAllcountUserCredential(user, registrationRequest));

        allcountUserRepository.save(user);

        return new RegistrationResponseDto(user.getId());
    }

    /**
     * Creates a new user details.
     *
     * @param registrationRequest the registration request
     */
    private AllcountUserDetailsEntity createAllcountUserDetails(AllcountUser user,
                                                                RegistrationRequestDto registrationRequest) {
        AllcountUserDetailsEntity allcountUserDetailsEntity = new AllcountUserDetailsEntity();
        allcountUserDetailsEntity.setEmail(registrationRequest.getEmail());
        allcountUserDetailsEntity.setFirstName(registrationRequest.getFirstName());
        allcountUserDetailsEntity.setLastName(registrationRequest.getLastName());
        allcountUserDetailsEntity.setUser(user);

        return allcountUserDetailsEntity;
    }

    /**
     * Creates a new user credential.
     *
     * @param registrationRequest the registration request
     */
    private UserCredential createAllcountUserCredential(AllcountUser user, RegistrationRequestDto registrationRequest) {
        UserCredential userCredential = new UserCredential();
        HashedPassword hashedPassword = hashedPasswordService.hash(new Password(registrationRequest.getPassword()));
        userCredential.setUsername(registrationRequest.getUsername());
        userCredential.setPassword(hashedPassword);
        userCredential.setUser(user);

        return userCredential;
    }
}
