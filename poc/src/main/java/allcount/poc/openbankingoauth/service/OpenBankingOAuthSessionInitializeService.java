package allcount.poc.openbankingoauth.service;

import allcount.poc.openbankingoauth.entity.OpenBankingSessionEntity;
import allcount.poc.openbankingoauth.library.CodeVerifierLibrary;
import allcount.poc.openbankingoauth.mapper.OpenBankingBankToAuthorisationPathUriMapper;
import allcount.poc.openbankingoauth.mapper.OpenBankingBankToBaseUriMapper;
import allcount.poc.openbankingoauth.mapper.OpenBankingBankToSimulationMapper;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.object.enums.OpenBankingOAuthSessionStatusEnum;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthSessionRepository;
import allcount.poc.user.entity.AllcountUser;
import allcount.poc.user.repository.AllcountUserRepository;
import jakarta.ws.rs.core.UriBuilder;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for initializing the OpenBankingSessionEntity.
 */
@Service
public class OpenBankingOAuthSessionInitializeService extends OpenBankingOAuthService {
    private static final String PARAM_RESPONSE_TYPE = "response_type";
    private static final String PARAM_CODE_CHALLENGE_METHOD = "code_challenge_method";
    private static final String PARAM_CODE_CHALLENGE = "code_challenge";
    private static final String PARAM_STATE = "state";
    private static final String RESPONSE_TYPE_CODE = "code";
    private static final String SCOPE_READ_ACCOUNTS = "read_accounts";
    private static final String SCOPE_READ_TRANSACTIONS = "read_transactions";
    private static final String SCOPE_OFFLINE_ACCESS = "offline_access";
    private static final String CODE_CHALLENGE_METHOD = "S256";
    private final OpenBankingBankToAuthorisationPathUriMapper openBankingBankToAuthorisationPathUriMapper;

    /**
     * Constructor.
     *
     * @param userRepository                    - the AllcountUserRepository
     * @param openBankingOAuthSessionRepository - the OpenBankingOAuthSessionRepository
     */
    @Autowired
    public OpenBankingOAuthSessionInitializeService(
            UserDetailsService userDetailsService,
            AllcountUserRepository userRepository,
            OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository,
            OpenBankingBankToBaseUriMapper openBankingBankToBaseUriMapper,
            OpenBankingBankToAuthorisationPathUriMapper openBankingBankToAuthorisationPathUriMapper,
            OpenBankingBankToSimulationMapper openBankingBankToSimulationMapper
    ) {
        super(userDetailsService, userRepository, openBankingOAuthSessionRepository, openBankingBankToBaseUriMapper,  openBankingBankToSimulationMapper);
        this.openBankingBankToAuthorisationPathUriMapper = openBankingBankToAuthorisationPathUriMapper;
    }

    /**
     * Initializes the OpenBankingSessionEntity.
     *
     * @param userId - the userId
     * @param bank   - the bank
     * @return the OpenBankingSessionEntity
     */
    @Transactional
    public OpenBankingSessionEntity initializeOpenBankingOAuthSession(UUID userId, OpenBankingBankEnum bank) {
        AllcountUser user = userRepository.findById(userId).orElseThrow();

        assertUserAuthenticatedToActOnBehalfOfUser(user);

        UUID state = UUID.randomUUID();
        String codeVerifier = CodeVerifierLibrary.generateRandomCodeVerifier();
        String codeChallenge = CodeVerifierLibrary.generateCodeChallenge(codeVerifier);
        String redirectLoginUri = generateOauthLoginUri(bank, codeChallenge, state);

        return createOpenBankingOAuthSession(bank, codeVerifier, state, user, redirectLoginUri);
    }

    /**
     * Generates the OAuth login URI.
     *
     * @param bank          - the bank
     * @param codeChallenge - the codeChallenge
     * @param state         - the state
     * @return the OAuth login URI
     */
    private String generateOauthLoginUri(OpenBankingBankEnum bank, String codeChallenge, UUID state) {
        String baseUrl = openBankingBankToBaseUriMapper.getBaseUri(bank);
        String authorizationPath = openBankingBankToAuthorisationPathUriMapper.getAuthorisationPathUri(bank);
        String simulationClientId = openBankingBankToSimulationMapper.mapToSimulationId(bank);

        return UriBuilder.fromUri(baseUrl + authorizationPath)
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
     * Creates the OpenBankingSessionEntity.
     *
     * @param bank             - the bank
     * @param codeVerifier     - the codeVerifier
     * @param state            - the state
     * @param user             - the user
     * @param redirectLoginUri - the redirectLoginUri
     * @return the OpenBankingSessionEntity
     */
    private OpenBankingSessionEntity createOpenBankingOAuthSession(
            OpenBankingBankEnum bank,
            String codeVerifier,
            UUID state,
            AllcountUser user,
            String redirectLoginUri
    ) {
        OpenBankingSessionEntity session = OpenBankingSessionEntity.builder()
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
