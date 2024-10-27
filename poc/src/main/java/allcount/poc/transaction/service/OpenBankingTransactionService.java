package allcount.poc.transaction.service;

import allcount.poc.cashaccount.entity.CashAccountEntity;
import allcount.poc.cashaccount.repository.CashAccountRepository;
import allcount.poc.openbankingoauth.entity.OpenBankingOAuthAccessTokenRedisEntity;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.service.OpenBankingOAuthAccessTokenDetermineService;
import allcount.poc.transaction.client.TransactionRetrievalClient;
import allcount.poc.transaction.entity.TransactionEntity;
import allcount.poc.transaction.mapper.OpenBankingBankToTransactionUriMapper;
import allcount.poc.transaction.mapper.TransactionDtoToEntityMapper;
import allcount.poc.transaction.object.dto.TransactionDto;
import allcount.poc.transaction.repository.TransactionRepository;
import allcount.poc.user.entity.AllcountUser;
import allcount.poc.user.repository.AllcountUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service for syncing transactions from Bank APIs.
 */
@Service
public class OpenBankingTransactionService {
    private static final int DEFAULT_PAGE_SIZE = 100;
    private static final Logger LOG = Logger.getLogger(OpenBankingTransactionService.class.getName());
    private final transient CashAccountRepository cashAccountRepository;
    private final transient OpenBankingOAuthAccessTokenDetermineService openBankingOAuthAccessTokenDetermineService;
    private final transient TransactionRetrievalClient transactionRetrievalClient;
    private final transient TransactionDtoToEntityMapper transactionDtoToEntityMapper;
    private final TransactionRepository transactionRepository;
    private final AllcountUserRepository allcountUserRepository;
    private final OpenBankingBankToTransactionUriMapper openBankingBankToTransactionUriMapper;

    /**
     * Constructor.
     */
    @Autowired
    public OpenBankingTransactionService(
            CashAccountRepository cashAccountRepository,
            OpenBankingOAuthAccessTokenDetermineService openBankingOAuthAccessTokenDetermineService,
            TransactionRetrievalClient transactionRetrievalClient,
            TransactionDtoToEntityMapper transactionDtoToEntityMapper,
            TransactionRepository transactionRepository,
            AllcountUserRepository allcountUserRepository,
            OpenBankingBankToTransactionUriMapper openBankingBankToTransactionUriMapper) {
        this.cashAccountRepository = cashAccountRepository;
        this.openBankingOAuthAccessTokenDetermineService = openBankingOAuthAccessTokenDetermineService;
        this.transactionRetrievalClient = transactionRetrievalClient;
        this.transactionDtoToEntityMapper = transactionDtoToEntityMapper;
        this.transactionRepository = transactionRepository;
        this.allcountUserRepository = allcountUserRepository;
        this.openBankingBankToTransactionUriMapper = openBankingBankToTransactionUriMapper;
    }

    /**
     * Syncs transactions for a user. <br>
     * Fetches users transactions from the bank API and saves/updates them to/in the database.
     *
     * @param userId User ID to sync transactions for.
     * @param from   From date to sync transactions from. If null, syncs from epoch 0.
     * @param to     To date to sync transactions to. If null, syncs till now.
     */
    @Transactional
    public void syncTransactionsForUser(@NonNull UUID userId, @Nullable LocalDate from, @Nullable LocalDate to) {
        if (from == null) {
            from = LocalDate.ofEpochDay(0);
        }

        if (to == null) {
            to = LocalDate.now();
        }
        LOG.info("Syncing transactions for user: " + userId + " from: " + from + " till" + to);
        AllcountUser user = this.allcountUserRepository.findById(userId).orElseThrow();
        List<CashAccountEntity> accounts = cashAccountRepository.findAllByUser(user);
        LOG.info("Found " + accounts.size() + " accounts for user: " + userId);

        // todo: make the function return a list of statuses (number of synced transactions/failure) for each account
        for (CashAccountEntity account : accounts) {
            try {
                syncTransactionsForAccount(account.getId(), from, to);
                LOG.info("Successfully synced transactions for account: " + account.getId());
            } catch (Exception e) {
                LOG.severe(
                        "Failed to sync transactions for account: " + account.getId() + " due to: " + e.getMessage());
            }
        }
    }

    /**
     * Retrieve all transactions for the user.
     *
     * @param userId user id to sync transactions for.
     */
    public List<TransactionEntity> getAllTransactionByUser(UUID userId) {
        AllcountUser user = this.allcountUserRepository.findById(userId).orElseThrow();

        return transactionRepository.findAllByUser(user);
    }

    /**
     * Syncs transactions for an account. <br>
     * Fetches transactions from the bank API and saves/updates them to/in the database.
     *
     * @param accountId Account ID to sync transactions for.
     * @param from      From date to sync transactions from. If null, syncs from epoch 0.
     * @param to        To date to sync transactions to. If null, syncs till now.
     * @throws JsonProcessingException If the transactions cannot be parsed.
     */
    @Transactional
    void syncTransactionsForAccount(@NonNull UUID accountId, @Nullable LocalDate from, @Nullable LocalDate to)
            throws JsonProcessingException {
        LOG.info("Syncing transactions for account: " + accountId + (from != null ? " from: " + from : "")
                + (to != null ? " to: " + to : ""));
        CashAccountEntity account = cashAccountRepository.findById(accountId).orElseThrow();
        AllcountUser user = account.getUser();
        OpenBankingBankEnum bank = account.getBank();

        if (!openBankingBankToTransactionUriMapper.doesBankSupportTransactionRetrieval(bank)) {
            LOG.info("Bank: " + bank + " does not support transaction retrieval.");
            return;
        }

        OpenBankingOAuthAccessTokenRedisEntity tokenRedisEntity =
                openBankingOAuthAccessTokenDetermineService.determineAccessToken(user, bank);
        String accessToken = tokenRedisEntity.getAccessToken();

        int page = 0;
        boolean hasMore;
        do {
            List<TransactionDto> transactions = transactionRetrievalClient
                    .listTransactions(bank, account.getIban(), accessToken, from, to, DEFAULT_PAGE_SIZE, page);
            List<TransactionEntity> transactionEntities =
                    transactionDtoToEntityMapper.mapToEntities(transactions, List.of(account));

            transactionEntities.forEach(this::saveOrUpdateTransaction);
            transactionRepository.flush();

            hasMore = transactions.size() >= DEFAULT_PAGE_SIZE;
            page++;
        } while (hasMore);

        transactionRepository.flush();
    }

    /**
     * Saves or updates the transaction. <br>
     * If the transaction with the same external banking ID exists,
     * the existing transaction is updated with data from the new transaction.
     *
     * @param newTransaction New transaction to save or update.
     */
    @Transactional
    void saveOrUpdateTransaction(@NonNull TransactionEntity newTransaction) {
        Optional<TransactionEntity> existingTransactionOpt =
                transactionRepository.findByExternalBankingId(newTransaction.getExternalBankingId());

        if (existingTransactionOpt.isPresent()) {
            TransactionEntity existingTransaction = existingTransactionOpt.get();

            updateTransaction(newTransaction, existingTransaction);
            transactionRepository.save(existingTransaction);
        } else {
            transactionRepository.save(newTransaction);
        }
    }

    /**
     * Updates the target transaction fields with the source transaction. <br>
     * ID, createdTimestamp and entityHash are not copied.
     *
     * @param source Source transaction.
     * @param target Target transaction.
     */
    void updateTransaction(@NonNull TransactionEntity source, @NonNull TransactionEntity target) {
        BeanUtils.copyProperties(source, target, "id", "createdTimestamp", "entityHash");
    }
}