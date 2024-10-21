package allcount.poc.cashaccount.controller;

import allcount.poc.cashaccount.entity.CashAccountEntity;
import allcount.poc.cashaccount.mapper.CashAccountResponseMapper;
import allcount.poc.cashaccount.object.dto.CashAccountResponseDto;
import allcount.poc.cashaccount.service.CashAccountService;
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

    /**
     * Constructor.
     *
     * @param cashAccountService the service
     * @param cashAccountResponseMapper the mapper
     */
    @Autowired
    public CashAccountController(CashAccountService cashAccountService,
                                 CashAccountResponseMapper cashAccountResponseMapper) {
        this.cashAccountService = cashAccountService;
        this.cashAccountResponseMapper = cashAccountResponseMapper;
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
}
