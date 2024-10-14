package allcount.poc.openbankingoauth.service;

import allcount.poc.core.service.AllcountService;
import allcount.poc.openbankingoauth.mapper.OpenBankingBankToBaseUriMapper;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthSessionRepository;
import allcount.poc.user.repository.AllcountUserRepository;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Abstract class for OpenBankingOAuthService.
 */
@Service
public abstract class OpenBankingOAuthService extends AllcountService {
    protected static final String PARAM_SCOPE = "scope";
    protected static final String PARAM_CLIENT_ID = "client_id";
    protected static final String PARAM_REDIRECT_URI = "redirect_uri";
    protected static final String REDIRECT_URI =
            "https://localhost:8090/v1/open-banking-authorization/retrieve-access-token";

    protected static final String ERROR_SIMULATION_CLIENT_ID_NOT_FOUND = "Simulation client ID not found";
    protected static final String PLACEHOLDER_SIMULATION_CLIENT_ID = "simulation_client_id";
    protected static final String PLACEHOLDER_SIMULATION_CLIENT_SECRET = "simulation_client_secret";

    private static final Logger LOG = Logger.getLogger(OpenBankingOAuthService.class.getName());

    protected final transient Client client;
    protected final transient AllcountUserRepository userRepository;
    protected final transient OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository;
    protected final transient OpenBankingBankToBaseUriMapper openBankingBankToBaseUriMapper;

    @Value("#{environment.SIMULATION_CLIENT_ID}")
    protected String simulationClientId;

    @Value("#{environment.SIMULATION_CLIENT_SECRET}")
    protected String simulationClientSecret;


    /**
     * Constructor.
     *
     * @param userRepository                    - the AllcountUserRepository
     * @param openBankingOAuthSessionRepository - the OpenBankingOAuthSessionRepository
     */
    @Autowired
    public OpenBankingOAuthService(
            UserDetailsService userDetailsService,
            AllcountUserRepository userRepository,
            OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository,
            OpenBankingBankToBaseUriMapper openBankingBankToBaseUriMapper
    ) {
        super(userDetailsService);
        this.openBankingBankToBaseUriMapper = openBankingBankToBaseUriMapper;
        this.client = ClientBuilder.newBuilder().build().register(new LoggingFilter());
        this.userRepository = userRepository;
        this.openBankingOAuthSessionRepository = openBankingOAuthSessionRepository;

        if (simulationClientId == null || simulationClientId.isEmpty()) {
            LOG.log(Level.SEVERE, ERROR_SIMULATION_CLIENT_ID_NOT_FOUND);

            // TODO: try to refactor this and use env in tests as well.
            simulationClientId = PLACEHOLDER_SIMULATION_CLIENT_ID;
        }

        if (simulationClientSecret == null || simulationClientSecret.isEmpty()) {
            LOG.log(Level.SEVERE, ERROR_SIMULATION_CLIENT_ID_NOT_FOUND);

            // TODO: try to refactor this and use env in tests as well.
            simulationClientSecret = PLACEHOLDER_SIMULATION_CLIENT_SECRET;
        }
    }

}
