package allcount.poc.openbankingoauth.service;

import allcount.poc.openbankingoauth.entity.OpenBankingOAuthSessionEntity;
import allcount.poc.openbankingoauth.library.CodeVerifierLibrary;
import allcount.poc.openbankingoauth.mapper.OpenBankingBankToBaseUriMapper;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.object.enums.OpenBankingOAuthSessionStatusEnum;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthSessionRepository;
import allcount.poc.user.entity.AllcountUser;
import allcount.poc.user.repository.AllcountUserRepository;
import jakarta.ws.rs.core.UriBuilder;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for initializing the OpenBankingOAuthSessionEntity.
 */
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

    /**
     * Constructor.
     *
     * @param userRepository                    - the AllcountUserRepository
     * @param openBankingOAuthSessionRepository - the OpenBankingOAuthSessionRepository
     */
    @Autowired
    public OpenBankingOAuthSessionInitializeService(
            AllcountUserRepository userRepository,
            OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository,
            OpenBankingBankToBaseUriMapper openBankingBankToBaseUriMapper
    ) {
        super(userRepository, openBankingOAuthSessionRepository, openBankingBankToBaseUriMapper);
    }

    /**
     * Initializes the OpenBankingOAuthSessionEntity.
     *
     * @param userId - the userId
     * @param bank   - the bank
     * @return the OpenBankingOAuthSessionEntity
     */
    @Transactional
    public OpenBankingOAuthSessionEntity initializeOpenBankingOAuthSession(UUID userId, OpenBankingBankEnum bank) {
        AllcountUser user = userRepository.findById(userId).orElseThrow();
        String baseUrl = openBankingBankToBaseUriMapper.getBaseUri(bank);

        UUID state = UUID.randomUUID();
        String codeVerifier = CodeVerifierLibrary.generateRandomCodeVerifier();
        String codeChallenge = CodeVerifierLibrary.generateCodeChallenge(codeVerifier);
        String redirectLoginUri = generateOauthLoginUri(baseUrl, codeChallenge, state);

        return createOpenBankingOAuthSession(bank, codeVerifier, state, user, redirectLoginUri);
    }

    /**
     * Generates the OAuth login URI.
     *
     * @param baseUrl       - the baseUrl
     * @param codeChallenge - the codeChallenge
     * @param state         - the state
     * @return the OAuth login URI
     */
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

    /**
     * Gets the scope. TODO: Maybe can be enum.
     *
     * @return the scope
     */
    private String getScope() {
        List<String> scopes = List.of(SCOPE_READ_ACCOUNTS, SCOPE_READ_TRANSACTIONS, SCOPE_OFFLINE_ACCESS);
        return String.join(" ", scopes);
    }

    /**
     * Creates the OpenBankingOAuthSessionEntity.
     *
     * @param bank             - the bank
     * @param codeVerifier     - the codeVerifier
     * @param state            - the state
     * @param user             - the user
     * @param redirectLoginUri - the redirectLoginUri
     * @return the OpenBankingOAuthSessionEntity
     */
    private OpenBankingOAuthSessionEntity createOpenBankingOAuthSession(
            OpenBankingBankEnum bank,
            String codeVerifier,
            UUID state,
            AllcountUser user,
            String redirectLoginUri
    ) {
        OpenBankingOAuthSessionEntity session = OpenBankingOAuthSessionEntity.builder()
                .bank(bank)
                .user(user)
                .status(OpenBankingOAuthSessionStatusEnum.OAUTH_URI_GENERATED)
                .codeVerifier(codeVerifier)
                .state(state)
                .redirectLoginUri(redirectLoginUri)
                .build();

        return openBankingOAuthSessionRepository.save(session);
    }

}
