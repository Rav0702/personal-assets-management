package allcount.poc.open_banking_authorization.controller;

import allcount.poc.open_banking_authorization.mapper.OpenBankingOAuthAccessTokenResponseMapper;
import allcount.poc.open_banking_authorization.mapper.OpenBankingOAuthSessionResponseMapper;
import allcount.poc.open_banking_authorization.object.OpenBankingBankEnum;
import allcount.poc.open_banking_authorization.object.OpenBankingOAuthAccessTokenResponseDto;
import allcount.poc.open_banking_authorization.object.OpenBankingOAuthSessionResponseDto;
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
    private final transient OpenBankingOAuthSessionResponseMapper openBankingOAuthSessionResponseMapper;

    @Autowired
    public OpenBankingOAuthController(
            OpenBankingOAuthSessionInitializeService openBankingOAuthSessionInitializeService,
            OpenBankingOAuthAccessTokenRetrieveService openBankingOAuthAccessTokenRetrieveService,
            OpenBankingOAuthAccessTokenResponseMapper openBankingOAuthAccessTokenResponseMapper,
            OpenBankingOAuthSessionResponseMapper openBankingOAuthSessionResponseMapper
    ) {
        this.openBankingOAuthSessionInitializeService = openBankingOAuthSessionInitializeService;
        this.openBankingOAuthAccessTokenRetrieveService = openBankingOAuthAccessTokenRetrieveService;
        this.openBankingOAuthAccessTokenResponseMapper = openBankingOAuthAccessTokenResponseMapper;
        this.openBankingOAuthSessionResponseMapper = openBankingOAuthSessionResponseMapper;
    }

    @GetMapping("v1/open-banking-authorization/{userId}/initialize-session")
    public OpenBankingOAuthSessionResponseDto generateOAuthUri(@PathVariable UUID userId, @RequestParam OpenBankingBankEnum bank) {
        return openBankingOAuthSessionResponseMapper.mapToOpenBankingOAuthSessionResponse(
                openBankingOAuthSessionInitializeService.initializeOpenBankingOAuthSession(userId, bank)
        );
    }

    @GetMapping("v1/open-banking-authorization/retrieve-access-token")
    public OpenBankingOAuthAccessTokenResponseDto generateCodeChallenge(@RequestParam String code, @RequestParam UUID state)
            throws JsonProcessingException {
        return openBankingOAuthAccessTokenResponseMapper.mapToOpenBankingOAuthAccessTokenResponse(
                openBankingOAuthAccessTokenRetrieveService.retrieveOpenBankingOAuthAccessToken(code, state)
        );
    }


}
