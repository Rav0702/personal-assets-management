package allcount.poc.open_banking_authorization.service;

import allcount.poc.open_banking_authorization.entity.OpenBankingOAuthSessionEntity;
import allcount.poc.open_banking_authorization.library.CodeVerifierLibrary;
import allcount.poc.open_banking_authorization.object.OpenBankingBankEnum;
import allcount.poc.open_banking_authorization.object.OpenBankingOAuthSessionStatusEnum;
import allcount.poc.open_banking_authorization.repository.OpenBankingOAuthSessionRepository;
import allcount.poc.user.entity.AllcountUser;
import allcount.poc.user.repository.AllcountUserRepository;
import jakarta.ws.rs.core.UriBuilder;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OpenBankingOAuthSessionInitializeService extends OpenBankingOAuthService {
    private static final String AUTHORIZATION_URL = "/gw/oidc/authorize";

    private static final String PARAM_RESPONSE_TYPE = "response_type";
    private static final String PARAM_SCOPE = "scope";
    private static final String PARAM_CODE_CHALLENGE_METHOD = "code_challenge_method";
    private static final String PARAM_CODE_CHALLENGE = "code_challenge";
    private static final String PARAM_STATE = "state";
    private static final String RESPONSE_TYPE_CODE = "code";
    private static final String SCOPE_READ_ACCOUNTS = "read_accounts";
    private static final String SCOPE_READ_TRANSACTIONS = "read_transactions";
    private static final String SCOPE_OFFLINE_ACCESS = "offline_access";
    private static final String CODE_CHALLENGE_METHOD = "S256";

    @Autowired
    public OpenBankingOAuthSessionInitializeService(
            AllcountUserRepository userRepository,
            OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository) {
        super(userRepository, openBankingOAuthSessionRepository);
    }

    @Transactional
    public String initializeOpenBankingOAuthSession(UUID userId, OpenBankingBankEnum bank) {
        AllcountUser user = userRepository.findById(userId).orElseThrow();
        String baseUrl = bank.getBaseUri();

        UUID state = UUID.randomUUID();
        String codeVerifier = CodeVerifierLibrary.generateRandomCodeVerifier();
        String codeChallenge = CodeVerifierLibrary.generateCodeChallenge(codeVerifier);

        createOpenBankingOAuthSession(bank, codeVerifier, state, user);

        return generateOauthLoginUri(baseUrl, codeChallenge, state);
    }

    private void createOpenBankingOAuthSession(OpenBankingBankEnum bank, String codeVerifier, UUID state, AllcountUser user) {
        OpenBankingOAuthSessionEntity session = new OpenBankingOAuthSessionEntity();
        session.setBank(bank);
        session.setStatus(OpenBankingOAuthSessionStatusEnum.OAUTH_URI_GENERATED);
        session.setCodeVerifier(codeVerifier);
        session.setState(state);
        session.setUser(user);

        openBankingOAuthSessionRepository.save(session);
    }

    private String generateOauthLoginUri(String baseUrl, String codeChallenge, UUID state) {
        return UriBuilder.fromUri(baseUrl + AUTHORIZATION_URL)
                .queryParam(PARAM_RESPONSE_TYPE, RESPONSE_TYPE_CODE)
                .queryParam(PARAM_CLIENT_ID, simulationClientId)
                .queryParam(PARAM_REDIRECT_URI, REDIRECT_URI)
                .queryParam(PARAM_SCOPE, getScope())
                .queryParam(PARAM_CODE_CHALLENGE_METHOD, CODE_CHALLENGE_METHOD)
                .queryParam(PARAM_CODE_CHALLENGE, codeChallenge)
                .queryParam(PARAM_STATE, state.toString())
                .build()
                .toString();
    }

    private String getScope() {
        List<String> scopes = List.of(SCOPE_READ_ACCOUNTS, SCOPE_READ_TRANSACTIONS, SCOPE_OFFLINE_ACCESS);
        return String.join(" ", scopes);
    }

}
