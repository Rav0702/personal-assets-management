package allcount.poc.openbankingoauth.mapper;

import allcount.poc.openbankingoauth.entity.OpenBankingSessionEntity;
import allcount.poc.openbankingoauth.object.dto.OpenBankingOAuthSessionResponseDto;
import org.springframework.stereotype.Component;

/**
 * Mapper for the OpenBankingSessionEntity.
 */
@Component
public class OpenBankingOAuthSessionResponseMapper {

    /**
     * Maps the OpenBankingSessionEntity to the OpenBankingOAuthSessionResponseDto.
     *
     * @param openBankingOAuthSession - the OpenBankingSessionEntity
     * @return the OpenBankingOAuthSessionResponseDto
     */
    public OpenBankingOAuthSessionResponseDto mapToOpenBankingOAuthSessionResponse(
            OpenBankingSessionEntity openBankingOAuthSession) {
        return OpenBankingOAuthSessionResponseDto.builder()
                .id(openBankingOAuthSession.getId())
                .userId(openBankingOAuthSession.getUser().getId())
                .bank(openBankingOAuthSession.getBank())
                .status(openBankingOAuthSession.getStatus())
                .redirectLoginUri(openBankingOAuthSession.getRedirectLoginUri())
                .codeVerifier(openBankingOAuthSession.getCodeVerifier())
                .state(openBankingOAuthSession.getState())
                .build();
    }
}
