package allcount.poc.transaction.controller;

import allcount.poc.transaction.object.dto.TransactionListDto;
import allcount.poc.transaction.service.OpenBankingTransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for syncing transactions.
 */
@RestController
@RequestMapping(path = "v1/transaction/sync")
public class TransactionSyncController {
    private final transient OpenBankingTransactionService transactionService;

    /**
     * Constructor.
     */
    @Autowired
    public TransactionSyncController(OpenBankingTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * JavaDoc todo.
     *
     * @param accountId doc todo
     * @return doc todo
     * @throws JsonProcessingException doc todo
     */
    @GetMapping("{accountId}")
    public TransactionListDto syncTransactionsForAccount(@NonNull @PathVariable UUID accountId) throws JsonProcessingException {
        return transactionService.fetchTransactionsForAccount(accountId);
    }
}
