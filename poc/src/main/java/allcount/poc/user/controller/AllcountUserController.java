package allcount.poc.user.controller;

import allcount.poc.credential.entity.UserCredential;
import allcount.poc.credential.object.Password;
import allcount.poc.credential.repository.UserCredentialRepository;
import allcount.poc.credential.service.HashedPasswordService;
import allcount.poc.user.entity.AllcountUser;
import allcount.poc.user.repository.AllcountUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for the AllcountUser entity.
 */
@Controller
@RequestMapping(path = "/v1/user")
public class AllcountUserController {

    private final transient AllcountUserRepository userRepository;
    private final transient UserCredentialRepository userCredentialRepository;
    private final transient HashedPasswordService hashedPasswordService;

    /**
     * Constructor.
     *
     * @param userRepository           the repository for the AllcountUser entity.
     * @param userCredentialRepository the repository for the UserCredential entity.
     * @param hashedPasswordService    the service for hashing passwords.
     */
    @Autowired
    public AllcountUserController(
            AllcountUserRepository userRepository,
            UserCredentialRepository userCredentialRepository,
            HashedPasswordService hashedPasswordService
    ) {
        this.userRepository = userRepository;
        this.userCredentialRepository = userCredentialRepository;
        this.hashedPasswordService = hashedPasswordService;
    }

    /**
     * Adds a new user to the system.
     *
     * @return a string indicating that the operation was successful.
     */
    @PostMapping(path = "/add")
    public @ResponseBody String addNewUser() {
        AllcountUser allcountUser = new AllcountUser();
        UserCredential userCredential = new UserCredential();

        userRepository.save(allcountUser);

        userCredential.setUsername("username");
        userCredential.setPassword(hashedPasswordService.hash(new Password("password")));
        userCredential.setUser(allcountUser);

        userCredentialRepository.save(userCredential);
        return "It works!!";
    }
}
