package allcount.poc.core.service;

import allcount.poc.user.entity.AllcountUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Base class for all services in the application.
 */
@Service
public abstract class AllcountService {

    public static final String ERROR_USER_IS_NOT_ALLOWED_TO_PERFORM_THIS_OPERATION =
            "User is not allowed to perform this operation";
    protected final transient UserDetailsService userDetailsService;

    /**
     * Instantiates a new Allcount service.
     *
     * @param userDetailsService the user details service
     */
    @Autowired
    protected AllcountService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Asserts that the user is authenticated to act on behalf of the user.
     *
     * @param user the user
     */
    protected void assertUserAuthenticatedToActOnBehalfOfUser(AllcountUser user) {
        AllcountUser authenticatedUser = getAuthenticatedUser();

        if (!authenticatedUser.equals(user)) {
            throw new SecurityException(ERROR_USER_IS_NOT_ALLOWED_TO_PERFORM_THIS_OPERATION);
        }
    }

    /**
     * Gets the authenticated user.
     *
     * @return the authenticated user
     */
    protected AllcountUser getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        assert principal instanceof String;

        UserDetails user = userDetailsService.loadUserByUsername((String) principal);

        assert user instanceof AllcountUser;

        return (AllcountUser) user;
    }
}
