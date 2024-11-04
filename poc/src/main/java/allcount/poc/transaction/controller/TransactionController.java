package allcount.poc.transaction.controller;

import allcount.poc.transaction.mapper.TransactionEntityToDtoMapper;
import allcount.poc.transaction.object.dto.TransactionDto;
import allcount.poc.transaction.service.OpenBankingTransactionService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for retrieving transactions.
 */
@RestController
@RequestMapping(path = "v1/transaction/")
public class TransactionController {
    private final transient OpenBankingTransactionService transactionService;
    private final transient TransactionEntityToDtoMapper transactionEntityToDtoMapper;

    /**
     * Constructor.
     */
    @Autowired
    public TransactionController(OpenBankingTransactionService transactionService,
                                 TransactionEntityToDtoMapper transactionEntityToDtoMapper) {
        this.transactionService = transactionService;
        this.transactionEntityToDtoMapper = transactionEntityToDtoMapper;
    }

    /**
     * Retrieve all transactions for the user.
     *
     * @param userId user id to sync transactions for.
     */
    @GetMapping("/{userId}")
    public List<TransactionDto> syncAllTransactionsForAccount(@NonNull @PathVariable UUID userId) {
        return transactionEntityToDtoMapper.mapToDtos(transactionService.getAllTransactionByUser(userId));
    }
}
