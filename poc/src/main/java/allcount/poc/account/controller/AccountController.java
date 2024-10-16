package allcount.poc.account.controller;

import allcount.poc.account.entity.AccountEntity;
import allcount.poc.account.mapper.AccountResponseMapper;
import allcount.poc.account.object.dto.AccountResponseDto;
import allcount.poc.account.service.AccountService;
import allcount.poc.account.service.OpenBankingAccountService;
import allcount.poc.kafka.KafkaProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for authentication.
 */
@RestController
@RequestMapping(path = "v1/account")
public class AccountController {
    private final transient AccountService accountService;
    private final transient OpenBankingAccountService openBankingAccountService;
    private final transient AccountResponseMapper accountResponseMapper;
    private final transient KafkaProducerService kafkaProducerService;

    @Autowired
    public AccountController(AccountService accountService, OpenBankingAccountService openBankingAccountService, AccountResponseMapper accountResponseMapper,
                             KafkaProducerService kafkaProducerService) {
        this.accountService = accountService;
        this.openBankingAccountService = openBankingAccountService;
        this.accountResponseMapper = accountResponseMapper;
        this.kafkaProducerService = kafkaProducerService;
    }

    @GetMapping("retrieve-accounts/{userId}")
    public List<AccountResponseDto> retrieveAccounts(@NonNull @PathVariable UUID userId) throws JsonProcessingException {
        List<AccountEntity> accounts = openBankingAccountService.retrieveAccounts(userId);
        List<AccountResponseDto> returnedAccounts = new ArrayList<>();
        for (AccountEntity account : accounts) {
            returnedAccounts.add(accountResponseMapper.mapToAccountResponse(account));
        }
        return returnedAccounts;
    }

    @PostMapping("update")
    public void updateTransactions(@RequestBody String userId) throws JsonProcessingException {
        // Send message to Kafka topic
        kafkaProducerService.sendMessage("sync-job", userId);
    }
}
