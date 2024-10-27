package allcount.poc.kafka;

import allcount.poc.cashaccount.service.CashAccountService;
import allcount.poc.transaction.service.OpenBankingTransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Kafka consumer service.
 */
@Service
public class KafkaConsumerService {
    private final CashAccountService cashAccountService;
    private final OpenBankingTransactionService transactionService;
    private static final Logger LOG = Logger.getLogger(KafkaConsumerService.class.getName());

    /**
     * Constructor.
     *
     * @param cashAccountService the cash account service
     * @param transactionService the transaction service
     */
    public KafkaConsumerService(CashAccountService cashAccountService, OpenBankingTransactionService transactionService) {
        this.cashAccountService = cashAccountService;
        this.transactionService = transactionService;
    }

    /**
     * Consumes the message from the Kafka topic.
     *
     * @param syncMessageDto the message
     * @return the future
     * @throws JsonProcessingException if there is an error
     */
    @KafkaListener(topics = "sync-job", groupId = "banking-group")
    public CompletableFuture<String> consume(KafkaSyncMessageDto syncMessageDto) throws JsonProcessingException {
        LOG.info("Consumed message: " + syncMessageDto);

        if (syncMessageDto.isSyncAccounts()) {
            cashAccountService.retrieveAccounts(syncMessageDto.getUserId());
        }
        if (syncMessageDto.isSyncTransactions()) {
            transactionService.syncTransactionsForUser(syncMessageDto.getUserId(), syncMessageDto.getSyncFrom(), syncMessageDto.getSyncTo());
        }

        return CompletableFuture.completedFuture("Message consumed");
    }
}