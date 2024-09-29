package allcount.poc.user.controller;


import allcount.poc.user.entity.AllcountUser;
import allcount.poc.user.repository.AllcountUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/v1/user")
public class AllcountUserController {

    private final AllcountUserRepository userRepository;

    @Autowired
    public AllcountUserController(AllcountUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(path="/add")
    public @ResponseBody String addNewUser () {
        AllcountUser allcountUser = new AllcountUser();

        userRepository.save(allcountUser);
        return "It works";
    }
}
