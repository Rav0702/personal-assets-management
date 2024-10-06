package allcount.poc.open_banking_authorization.mapper;

import allcount.poc.open_banking_authorization.entity.OpenBankingOAuthSessionEntity;
import allcount.poc.open_banking_authorization.object.OpenBankingOAuthSessionResponseDto;
import org.springframework.stereotype.Component;

@Component
public class OpenBankingOAuthSessionResponseMapper {

        public OpenBankingOAuthSessionResponseDto mapToOpenBankingOAuthSessionResponse(OpenBankingOAuthSessionEntity openBankingOAuthSession) {
            OpenBankingOAuthSessionResponseDto openBankingOAuthSessionResponse = new OpenBankingOAuthSessionResponseDto();
            openBankingOAuthSessionResponse.setId(openBankingOAuthSession.getId());
            openBankingOAuthSessionResponse.setBank(openBankingOAuthSession.getBank());
            openBankingOAuthSessionResponse.setUserId(openBankingOAuthSession.getUser().getId());
            openBankingOAuthSessionResponse.setStatus(openBankingOAuthSession.getStatus());
            openBankingOAuthSessionResponse.setRedirectLoginUri(openBankingOAuthSession.getRedirectLoginUri());
            openBankingOAuthSessionResponse.setState(openBankingOAuthSession.getState());
            openBankingOAuthSessionResponse.setCodeVerifier(openBankingOAuthSession.getCodeVerifier());
            return openBankingOAuthSessionResponse;
        }
}
