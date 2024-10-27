package allcount.poc.kafka;

import allcount.poc.cashaccount.service.CashAccountService;
import allcount.poc.transaction.service.OpenBankingTransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.logging.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    private final CashAccountService cashAccountService;
    private final OpenBankingTransactionService transactionService;
    private static final Logger LOG = Logger.getLogger(KafkaConsumerService.class.getName());
    public KafkaConsumerService(CashAccountService cashAccountService, OpenBankingTransactionService transactionService) {
        this.cashAccountService = cashAccountService;
        this.transactionService = transactionService;
    }


    @KafkaListener(topics = "sync-job", groupId = "banking-group")
    public void consume(KafkaSyncMessageDto syncMessageDto) throws JsonProcessingException {
        // Log the message in Kafka
        LOG.info("Consumed message: " + syncMessageDto);

        if (syncMessageDto.isSyncAccounts()) {
            cashAccountService.retrieveAccounts(syncMessageDto.getUserId());
        }
        if (syncMessageDto.isSyncTransactions()) {
            transactionService.syncTransactionsForUser(syncMessageDto.getUserId(), syncMessageDto.getSyncFrom(), syncMessageDto.getSyncTo());
        }
    }
}