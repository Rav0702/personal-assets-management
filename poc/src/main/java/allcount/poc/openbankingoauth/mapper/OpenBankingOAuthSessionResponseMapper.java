package allcount.poc.openbankingoauth.mapper;

import allcount.poc.openbankingoauth.entity.OpenBankingOAuthSessionEntity;
import allcount.poc.openbankingoauth.object.dto.OpenBankingOAuthSessionResponseDto;
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
    public OpenBankingOAuthSessionResponseDto mapToOpenBankingOAuthSessionResponse(
            OpenBankingOAuthSessionEntity openBankingOAuthSession) {
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
