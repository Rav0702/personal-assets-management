package allcount.poc.account.service;

import allcount.poc.account.entity.AccountEntity;
import allcount.poc.account.mapper.AccountResponseMapper;
import allcount.poc.account.repository.AccountRepository;
import allcount.poc.openbankingoauth.entity.OpenBankingOAuthAccessTokenRedisEntity;
import allcount.poc.openbankingoauth.entity.OpenBankingOAuthEntity;
import allcount.poc.openbankingoauth.mapper.OpenBankingBankToBaseUriMapper;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.object.enums.OpenBankingOAuthSessionStatusEnum;
import allcount.poc.openbankingoauth.repository.OpenBankingOAuthSessionRepository;
import allcount.poc.openbankingoauth.service.LoggingFilter;
import allcount.poc.openbankingoauth.service.OpenBankingOAuthAccessTokenDetermineService;
import allcount.poc.user.entity.AllcountUser;
import allcount.poc.user.repository.AllcountUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.glassfish.jersey.client.ClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for retrieving the Accounts.
 */
@Service
public class OpenBankingAccountService {
    private static final String ACCOUNT_URL = "/gw/dbapi/banking/cashAccounts/v2";
    private static final String PARAM_REDIRECT_URI = "redirect_uri";
    private static final String REDIRECT_URI = "https://localhost:8090/v1/account/retrieve-accounts";
    private static final String PARAM_CLIENT_ID = "client_id";
    private static final String PARAM_CODE = "code";
    @Value("#{environment.SIMULATION_CLIENT_ID}")
    private String simulationClientId;
    private static final String PARAM_GRANT_TYPE = "grant_type";
    private static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
    private final transient Client client;

    private final transient AccountRepository accountRepository;
    private final transient OpenBankingBankToBaseUriMapper openBankingBankToBaseUriMapper;
    private final transient AccountResponseMapper accountResponseMapper;
    private final transient OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository;
    private final transient OpenBankingOAuthAccessTokenDetermineService openBankingOAuthAccessTokenDetermineService;
    private final transient AllcountUserRepository allcountUserRepository;

    /**
     * Constructor.
     *
     * @param accountRepository - the AccountRepository
     */
    @Autowired
    public OpenBankingAccountService(AccountRepository accountRepository,
                                     OpenBankingBankToBaseUriMapper openBankingBankToBaseUriMapper,
                                     AccountResponseMapper accountResponseMapper,
                                     OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository,
                                     OpenBankingOAuthAccessTokenDetermineService openBankingOAuthAccessTokenDetermineService, AllcountUserRepository allcountUserRepository) {
        this.accountRepository = accountRepository;
        this.accountResponseMapper = accountResponseMapper;
        this.openBankingOAuthSessionRepository = openBankingOAuthSessionRepository;
        this.allcountUserRepository = allcountUserRepository;
        this.client = ClientBuilder.newBuilder().build().register(new LoggingFilter());
        this.openBankingBankToBaseUriMapper = openBankingBankToBaseUriMapper;
        this.openBankingOAuthAccessTokenDetermineService = openBankingOAuthAccessTokenDetermineService;
    }

    /**
     * Retrieves a list of accounts for a specific user.
     *
     * @param userId - the UUID of the user
     * @return a list of {@link AccountEntity}
     * @throws JsonProcessingException if there is an issue processing the bank's JSON response
     */
    @Transactional
    public List<AccountEntity> retrieveAccounts(UUID userId) throws JsonProcessingException {
        AllcountUser user = allcountUserRepository.findById(userId).orElseThrow();
        List<OpenBankingBankEnum> banks = openBankingOAuthSessionRepository
                .findByUserIdAndStatus(userId, OpenBankingOAuthSessionStatusEnum.ACCESS_TOKEN_RECEIVED)
                .stream()
                .map(OpenBankingOAuthEntity::getBank)
                .toList();
        List<AccountEntity> accounts = new ArrayList<>();
        for (OpenBankingBankEnum bank : banks) {
            OpenBankingOAuthAccessTokenRedisEntity tokenRedisEntity = openBankingOAuthAccessTokenDetermineService.determineAccessToken(user, bank);
            String accessToken = tokenRedisEntity.getAccessToken();
            Response response = requestAccountFromCode(accessToken, bank);
            List<AccountEntity> currentAccounts = parseAccountFromOpenBankingResponse(response, user, bank);
            for (AccountEntity currentAccount : currentAccounts) {
                Optional<AccountEntity> existingAccount = accountRepository.findByIban(currentAccount.getIban());
                if (existingAccount.isEmpty()) {
                    accounts.add(currentAccount);
                } else {
                    existingAccount.get().setAccountType(currentAccount.getAccountType());
                    existingAccount.get().setBic(currentAccount.getBic());
                    existingAccount.get().setCurrencyCode(currentAccount.getCurrencyCode());
                    existingAccount.get().setCurrentBalance(currentAccount.getCurrentBalance());
                    existingAccount.get().setProductDescription(currentAccount.getProductDescription());
                    accounts.add(existingAccount.get());
                }
            }
        }
        accountRepository.saveAll(accounts);

        return accounts;
    }

    private Response requestAccountFromCode(String accessToken, OpenBankingBankEnum bank) {
        return client
                .property(ClientProperties.FOLLOW_REDIRECTS, Boolean.FALSE)
                .target(openBankingBankToBaseUriMapper.getBaseUri(bank) + ACCOUNT_URL)
                .request()
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .get();
    }

    private List<AccountEntity> parseAccountFromOpenBankingResponse(Response response, AllcountUser user, OpenBankingBankEnum bank) throws JsonProcessingException {
        return accountResponseMapper.mapToAccountEntities(response, user, bank);
    }
}
