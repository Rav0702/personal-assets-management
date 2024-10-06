package allcount.poc.openbankingoauth.mapper;

import allcount.poc.openbankingoauth.entity.OpenBankingOAuthSessionEntity;
import allcount.poc.openbankingoauth.object.OpenBankingOAuthSessionResponseDto;
import org.springframework.stereotype.Component;

/**
 * Mapper for the OpenBankingOAuthSessionEntity.
 */
@Component
public class OpenBankingOAuthSessionResponseMapper {

    /**
     * Maps the OpenBankingOAuthSessionEntity to the OpenBankingOAuthSessionResponseDto.
     *
     * @param openBankingOAuthSession - the OpenBankingOAuthSessionEntity
     * @return the OpenBankingOAuthSessionResponseDto
     */
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
