package allcount.poc.transaction.service;

import allcount.poc.account.entity.AccountEntity;
import allcount.poc.account.repository.AccountRepository;
import allcount.poc.openbankingoauth.entity.OpenBankingOAuthAccessTokenRedisEntity;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.service.OpenBankingOAuthAccessTokenDetermineService;
import allcount.poc.transaction.client.TransactionRetrievalClient;
import allcount.poc.transaction.entity.TransactionEntity;
import allcount.poc.transaction.mapper.TransactionDtoToEntityMapper;
import allcount.poc.transaction.object.dto.TransactionListDto;
import allcount.poc.transaction.repository.TransactionRepository;
import allcount.poc.user.entity.AllcountUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * todo
 */
@Service
public class OpenBankingTransactionService {
    private static final Logger LOG = Logger.getLogger(OpenBankingTransactionService.class.getName());
    private final transient AccountRepository accountRepository;
    private final transient OpenBankingOAuthAccessTokenDetermineService openBankingOAuthAccessTokenDetermineService;
    private final transient TransactionRetrievalClient transactionRetrievalClient;
    private final transient TransactionDtoToEntityMapper transactionDtoToEntityMapper;
    private final TransactionRepository transactionRepository;

    /**
     * Constructor.
     */
    @Autowired
    public OpenBankingTransactionService(
            AccountRepository accountRepository,
            OpenBankingOAuthAccessTokenDetermineService openBankingOAuthAccessTokenDetermineService,
            TransactionRetrievalClient transactionRetrievalClient,
            TransactionDtoToEntityMapper transactionDtoToEntityMapper,
            TransactionRepository transactionRepository
    ) {
        this.accountRepository = accountRepository;
        this.openBankingOAuthAccessTokenDetermineService = openBankingOAuthAccessTokenDetermineService;
        this.transactionRetrievalClient = transactionRetrievalClient;
        this.transactionDtoToEntityMapper = transactionDtoToEntityMapper;
        this.transactionRepository = transactionRepository;
    }


    /**
     * JavaDoc todo.
     * <b>FIXME AUTH</b>
     * @param accountId doc todo
     * @return doc todo
     * @throws JsonProcessingException doc todo
     */
    @Transactional
    public TransactionListDto fetchTransactionsForAccount(UUID accountId) throws JsonProcessingException {
        LOG.info("Fetching transactions for account: " + accountId);
        AccountEntity account = accountRepository.findById(accountId).orElseThrow();
        AllcountUser user = account.getUser();
        OpenBankingBankEnum bank = account.getBank();

        OpenBankingOAuthAccessTokenRedisEntity tokenRedisEntity = openBankingOAuthAccessTokenDetermineService.determineAccessToken(user, bank);
        String accessToken = tokenRedisEntity.getAccessToken();

        TransactionListDto transactions = transactionRetrievalClient.listTransactions(bank, account.getIban(), accessToken);
        List<TransactionEntity> transactionEntities = transactionDtoToEntityMapper.mapToEntities(transactions.getTransactions(), List.of(account));

        LOG.info("Saving " + transactionEntities.size() + " transactions to the database");

        transactionRepository.saveAllAndFlush(transactionEntities);
        return transactions;
    }


}
