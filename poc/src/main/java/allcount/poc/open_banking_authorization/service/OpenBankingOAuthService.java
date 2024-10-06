package allcount.poc.open_banking_authorization.service;


import allcount.poc.open_banking_authorization.repository.OpenBankingOAuthSessionRepository;
import allcount.poc.user.repository.AllcountUserRepository;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.NewCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public abstract class OpenBankingOAuthService {
    protected static final String BASE_URL = "https://simulator-api.db.com";
    protected static final String PARAM_CLIENT_ID = "client_id";
    protected static final String PARAM_REDIRECT_URI = "redirect_uri";
    protected static final String REDIRECT_URI = "https://localhost:8090/v1/open-banking-authorization/retrieve-access-token";


    @Value("#{environment.SIMULATION_CLIENT_ID}")
    protected String simulationClientId;

    protected final transient Client client;
    protected transient NewCookie sessionId;

    protected final transient AllcountUserRepository userRepository;
    protected final transient OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository;

    @Autowired
    public OpenBankingOAuthService(
            AllcountUserRepository userRepository,
            OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository
    ) {
        this.client = ClientBuilder.newBuilder().build().register(new LoggingFilter());
        this.userRepository = userRepository;
        this.openBankingOAuthSessionRepository = openBankingOAuthSessionRepository;

        assert simulationClientId != null && !simulationClientId.isEmpty();
    }

}
