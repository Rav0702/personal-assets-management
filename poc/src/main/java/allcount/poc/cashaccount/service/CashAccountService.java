package allcount.poc.cashaccount.service;

import allcount.poc.cashaccount.entity.CashAccountEntity;
import allcount.poc.cashaccount.mapper.CashAccountResponseMapper;
import allcount.poc.cashaccount.repository.CashAccountRepository;
import allcount.poc.core.domain.entity.OpenBankingEntity;
import allcount.poc.openbankingoauth.entity.OpenBankingOAuthAccessTokenRedisEntity;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for retrieving the Accounts.
 */
@Service
public class CashAccountService {
    private static final String ACCOUNT_URL = "/gw/dbapi/banking/cashAccounts/v2";
    private final transient Client client;

    private final transient CashAccountRepository cashAccountRepository;
    private final transient OpenBankingBankToBaseUriMapper openBankingBankToBaseUriMapper;
    private final transient CashAccountResponseMapper cashAccountResponseMapper;
    private final transient OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository;
    private final transient OpenBankingOAuthAccessTokenDetermineService openBankingOAuthAccessTokenDetermineService;
    private final transient AllcountUserRepository allcountUserRepository;

    /**
     * Constructor.
     *
     * @param cashAccountRepository - the CashAccountRepository
     */
    @Autowired
    public CashAccountService(CashAccountRepository cashAccountRepository,
                              OpenBankingBankToBaseUriMapper openBankingBankToBaseUriMapper,
                              CashAccountResponseMapper cashAccountResponseMapper,
                              OpenBankingOAuthSessionRepository openBankingOAuthSessionRepository,
                              OpenBankingOAuthAccessTokenDetermineService openBankingOAuthAccessTokenDetermineService,
                              AllcountUserRepository allcountUserRepository) {
        this.cashAccountRepository = cashAccountRepository;
        this.cashAccountResponseMapper = cashAccountResponseMapper;
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
     * @return a list of {@link CashAccountEntity}
     * @throws JsonProcessingException if there is an issue processing the bank's JSON response
     */
    @Transactional
    public List<CashAccountEntity> retrieveAccounts(UUID userId) throws JsonProcessingException {
        AllcountUser user = allcountUserRepository.findById(userId).orElseThrow();
        List<OpenBankingBankEnum> banks = openBankingOAuthSessionRepository
                .findByUserIdAndStatus(userId, OpenBankingOAuthSessionStatusEnum.ACCESS_TOKEN_RECEIVED)
                .stream()
                .map(OpenBankingEntity::getBank)
                .distinct()
                .toList();
        List<CashAccountEntity> accounts = new ArrayList<>();
        for (OpenBankingBankEnum bank : banks) {
            OpenBankingOAuthAccessTokenRedisEntity tokenRedisEntity =
                    openBankingOAuthAccessTokenDetermineService.determineAccessToken(user, bank);
            String accessToken = tokenRedisEntity.getAccessToken();
            Response response = requestAccountFromCode(accessToken, bank);
            List<CashAccountEntity> currentAccounts = parseAccountFromOpenBankingResponse(response, user, bank);
            for (CashAccountEntity currentAccount : currentAccounts) {
                Optional<CashAccountEntity> existingAccount =
                        cashAccountRepository.findByIban(currentAccount.getIban());
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
        cashAccountRepository.saveAll(accounts);

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

    private List<CashAccountEntity> parseAccountFromOpenBankingResponse(Response response, AllcountUser user,
                                                                        OpenBankingBankEnum bank)
            throws JsonProcessingException {
        return cashAccountResponseMapper.mapToAccountEntities(response, user, bank);
    }

    /**
     * Updates the banking records.
     *
     * @param message - the message
     */
    public void updateBankingRecords(String message) {
        // Process the message and call the OpenBanking API
        System.out.println("Message received: " + message);
    }
}
