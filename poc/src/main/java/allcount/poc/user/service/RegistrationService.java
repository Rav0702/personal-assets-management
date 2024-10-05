package allcount.poc.user.service;

import allcount.poc.credential.entity.AllcountUserDetailsEntity;
import allcount.poc.credential.object.HashedPassword;
import allcount.poc.credential.object.Password;
import allcount.poc.credential.service.HashedPasswordService;
import allcount.poc.user.entity.AllcountUser;
import allcount.poc.user.repository.AllcountUserRepository;
import allcount.poc.user.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;


@Service
public class RegistrationService {
    private final transient AllcountUserRepository allcountUserRepository;
    private final transient HashedPasswordService hashedPasswordService;

    private static final String apiPrefix = "http://localhost:";
    private static final String userPath = "/api/user";

    @Autowired
    private transient RestTemplate restTemplate;

    public RegistrationService(AllcountUserRepository allcountUserRepository, HashedPasswordService hashedPasswordService) {
        this.allcountUserRepository = allcountUserRepository;
        this.hashedPasswordService = hashedPasswordService;
    }


    public UUID registerUser(String email, Password password, String userName, String firstName, String lastName) throws Exception {
        try {
            if (userDetailsRepository.existsByUsername(userName)) {
                throw new RuntimeException("Email " + email + " is already taken");
            }

            // Hash password
            HashedPassword hashedPassword = hashedPasswordService.hash(password);

            // Create new account
            AllcountUserDetailsEntity user = new AllcountUserDetailsEntity();


            userRepository.save(user);
            return user;
        } catch (Exception e) {
            throw new Exception("Could not register user", e);
        }
    }

    public boolean checkEmailIsUnique(String email) {
        return !userDetailsRepository.existsByEmail(email);
    }
}
