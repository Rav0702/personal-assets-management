package allcount.poc.kafka;

import allcount.poc.account.service.OpenBankingAccountService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final OpenBankingAccountService openBankingAccountService;

    public KafkaConsumerService(OpenBankingAccountService openBankingAccountService) {
        this.openBankingAccountService = openBankingAccountService;
    }

    @KafkaListener(topics = "sync-job", groupId = "banking-group")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);

        // Process the message and call the OpenBanking API
        openBankingAccountService.updateBankingRecords(message);
    }
}