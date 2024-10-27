package allcount.poc.cashaccount.controller;

import allcount.poc.cashaccount.entity.CashAccountEntity;
import allcount.poc.cashaccount.mapper.CashAccountResponseMapper;
import allcount.poc.cashaccount.object.dto.CashAccountResponseDto;
import allcount.poc.cashaccount.service.CashAccountService;
import allcount.poc.kafka.KafkaProducerService;
import allcount.poc.kafka.KafkaSyncMessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for authentication.
 */
@RestController
@RequestMapping(path = "v1/cash-account")
public class CashAccountController {

    private final transient CashAccountService cashAccountService;
    private final transient CashAccountResponseMapper cashAccountResponseMapper;
    private final transient KafkaProducerService kafkaProducerService;
    private static final String TOPIC = "sync-job";

    /**
     * Constructor.
     *
     * @param cashAccountService        the service
     * @param cashAccountResponseMapper the mapper
     */
    @Autowired
    public CashAccountController(CashAccountService cashAccountService,
                                 CashAccountResponseMapper cashAccountResponseMapper,
                                 KafkaProducerService kafkaProducerService) {
        this.cashAccountService = cashAccountService;
        this.cashAccountResponseMapper = cashAccountResponseMapper;
        this.kafkaProducerService = kafkaProducerService;
    }

    /**
     * Retrieve the accounts.
     *
     * @param userId the user id
     * @return the list of accounts
     * @throws JsonProcessingException if there is an error
     */
    @GetMapping("{userId}")
    public List<CashAccountResponseDto> retrieveAccounts(@NonNull @PathVariable UUID userId)
            throws JsonProcessingException {
        List<CashAccountEntity> accounts = cashAccountService.retrieveAccounts(userId);
        List<CashAccountResponseDto> returnedAccounts = new ArrayList<>();
        for (CashAccountEntity account : accounts) {
            returnedAccounts.add(cashAccountResponseMapper.mapToAccountResponse(account));
        }
        return returnedAccounts;
    }

    /**
     * Send a request to Kafka to retrieve the accounts.
     *
     * @param userId the user id
     */
    @GetMapping("/kafka/{userId}")
    public void retrieveAccountsByKafka(@NonNull @PathVariable UUID userId) {
        KafkaSyncMessageDto message = new KafkaSyncMessageDto(userId, true, false, null, null);
        kafkaProducerService.sendMessage(TOPIC, message);
    }
}
