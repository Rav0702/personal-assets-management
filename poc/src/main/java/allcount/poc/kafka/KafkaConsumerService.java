package allcount.poc.kafka;

import allcount.poc.cashaccount.service.CashAccountService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final CashAccountService cashAccountService;

    public KafkaConsumerService(CashAccountService cashAccountService) {
        this.cashAccountService = cashAccountService;
    }

    @KafkaListener(topics = "sync-job", groupId = "banking-group")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);

        // Process the message and call the OpenBanking API
        cashAccountService.updateBankingRecords(message);
    }
}