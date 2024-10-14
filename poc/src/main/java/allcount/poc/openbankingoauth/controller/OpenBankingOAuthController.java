package allcount.poc.openbankingoauth.controller;

import allcount.poc.openbankingoauth.entity.OpenBankingOAuthAccessTokenRedisEntity;
import allcount.poc.openbankingoauth.mapper.OpenBankingOAuthSessionResponseMapper;
import allcount.poc.openbankingoauth.object.dto.OpenBankingOAuthSessionResponseDto;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.service.OpenBankingOAuthAccessTokenInitializeService;
import allcount.poc.openbankingoauth.service.OpenBankingOAuthSessionInitializeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for OpenBankingOAuth.
 */
@RestController
public class OpenBankingOAuthController {

    private final transient OpenBankingOAuthSessionInitializeService openBankingOAuthSessionInitializeService;
    private final transient OpenBankingOAuthAccessTokenInitializeService openBankingOAuthAccessTokenInitializeService;
    private final transient OpenBankingOAuthSessionResponseMapper openBankingOAuthSessionResponseMapper;

    /**
     * Constructor.
     *
     * @param openBankingOAuthSessionInitializeService     - the OpenBankingOAuthSessionInitializeService
     * @param openBankingOAuthAccessTokenInitializeService - the OpenBankingOAuthAccessTokenInitializeService
     * @param openBankingOAuthSessionResponseMapper        - the OpenBankingOAuthSessionResponseMapper
     */
    @Autowired
    public OpenBankingOAuthController(
            OpenBankingOAuthSessionInitializeService openBankingOAuthSessionInitializeService,
            OpenBankingOAuthAccessTokenInitializeService openBankingOAuthAccessTokenInitializeService,
            OpenBankingOAuthSessionResponseMapper openBankingOAuthSessionResponseMapper
    ) {
        this.openBankingOAuthSessionInitializeService = openBankingOAuthSessionInitializeService;
        this.openBankingOAuthAccessTokenInitializeService = openBankingOAuthAccessTokenInitializeService;
        this.openBankingOAuthSessionResponseMapper = openBankingOAuthSessionResponseMapper;
    }

    /**
     * Generates the OAuth URI.
     *
     * @param userId - the userId
     * @param bank   - the bank
     * @return the OpenBankingOAuthSessionResponseDto
     */
    @GetMapping("v1/open-banking-authorization/{userId}/initialize-session")
    public OpenBankingOAuthSessionResponseDto generateOAuthUri(
            @NonNull @PathVariable UUID userId,
            @NonNull @RequestParam OpenBankingBankEnum bank
    ) {
        return openBankingOAuthSessionResponseMapper.mapToOpenBankingOAuthSessionResponse(
                openBankingOAuthSessionInitializeService.initializeOpenBankingOAuthSession(userId, bank)
        );
    }

    /**
     * Retrieves the access token.
     *
     * @param code  - the code
     * @param state - the state
     * @return the OpenBankingOAuthAccessTokenResponseDto
     * @throws JsonProcessingException - if an error occurs
     */
    @GetMapping("v1/open-banking-authorization/retrieve-access-token")
    public OpenBankingOAuthAccessTokenRedisEntity retrieveAccessToken(
            @NonNull @RequestParam String code,
            @NonNull @RequestParam UUID state
    ) throws JsonProcessingException {
        return openBankingOAuthAccessTokenInitializeService.initializeOpenBankingOAuthAccessToken(code, state);

    }
}
