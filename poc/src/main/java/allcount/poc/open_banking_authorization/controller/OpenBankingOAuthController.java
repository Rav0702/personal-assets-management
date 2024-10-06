package allcount.poc.open_banking_authorization.controller;

import allcount.poc.open_banking_authorization.object.OpenBankingBankEnum;
import allcount.poc.open_banking_authorization.object.OpenBankingOAuthAccessTokenResponseDto;
import allcount.poc.open_banking_authorization.service.OpenBankingOAuthAccessTokenResponseMapper;
import allcount.poc.open_banking_authorization.service.OpenBankingOAuthAccessTokenRetrieveService;
import allcount.poc.open_banking_authorization.service.OpenBankingOAuthSessionInitializeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenBankingOAuthController {

    private final transient OpenBankingOAuthSessionInitializeService openBankingOAuthSessionInitializeService;
    private final transient OpenBankingOAuthAccessTokenRetrieveService openBankingOAuthAccessTokenRetrieveService;
    private final transient OpenBankingOAuthAccessTokenResponseMapper openBankingOAuthAccessTokenResponseMapper;

    @Autowired
    public OpenBankingOAuthController(
            OpenBankingOAuthSessionInitializeService openBankingOAuthSessionInitializeService,
            OpenBankingOAuthAccessTokenRetrieveService openBankingOAuthAccessTokenRetrieveService, OpenBankingOAuthAccessTokenResponseMapper openBankingOAuthAccessTokenResponseMapper
    ) {
        this.openBankingOAuthSessionInitializeService = openBankingOAuthSessionInitializeService;
        this.openBankingOAuthAccessTokenRetrieveService = openBankingOAuthAccessTokenRetrieveService;
        this.openBankingOAuthAccessTokenResponseMapper = openBankingOAuthAccessTokenResponseMapper;
    }

    @GetMapping("v1/open-banking-authorization/{userId}/generate-oauth-uri")
    public String generateOAuthUri(@PathVariable UUID userId, @RequestParam OpenBankingBankEnum bank) {
        return openBankingOAuthSessionInitializeService.initializeOpenBankingOAuthSession(userId, bank);
    }

    @GetMapping("v1/open-banking-authorization/code-challenge")
    public OpenBankingOAuthAccessTokenResponseDto generateCodeChallenge(@RequestParam String code, @RequestParam UUID state)
            throws JsonProcessingException {
        return openBankingOAuthAccessTokenResponseMapper.mapToOpenBankingOAuthAccessTokenResponse(
                openBankingOAuthAccessTokenRetrieveService.retrieveOpenBankingOAuthAccessToken(code, state)
        );
    }


}
