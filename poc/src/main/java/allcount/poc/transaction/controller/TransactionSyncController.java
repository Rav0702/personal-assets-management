package allcount.poc.transaction.controller;

import allcount.poc.kafka.KafkaProducerService;
import allcount.poc.kafka.KafkaSyncMessageDto;
import allcount.poc.transaction.service.OpenBankingTransactionService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for syncing transactions. <br>
 * This controller is used for testing purposes only.
 * Syncing transactions should be done by scheduled tasks or external call (see R2).
 */
@RestController
@RequestMapping(path = "v1/transaction/sync")
public class TransactionSyncController {
    private final transient OpenBankingTransactionService transactionService;
    private final transient KafkaProducerService kafkaProducerService;
    private static final String TOPIC = "sync-job";
    /**
     * Constructor.
     */
    @Autowired
    public TransactionSyncController(OpenBankingTransactionService transactionService,
                                     KafkaProducerService kafkaProducerService) {
        this.transactionService = transactionService;
        this.kafkaProducerService = kafkaProducerService;
    }

    /**
     * Sync all transactions for the account. <br>
     * This method is used for testing purposes only.
     *
     * @param userId user id to sync transactions for.
     */
    @GetMapping("/{userId}/all")
    public String syncAllTransactionsForAccount(@NonNull @PathVariable UUID userId) {
        transactionService.syncTransactionsForUser(userId, null, null);
        return "";
    }

    /**
     *  Sync all transactions for the account using Kafka.
     *
     *  @param userId user id to sync transactions for.
     */
    @GetMapping("kafka/{userId}/all")
    public void syncAllTransactionsForAccountKafka(@NonNull @PathVariable UUID userId) {
        KafkaSyncMessageDto syncMessageDto = new KafkaSyncMessageDto(userId, false, true, null, null);
        kafkaProducerService.sendMessage(TOPIC, syncMessageDto);
    }
}
