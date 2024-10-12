package allcount.poc.openbankingoauth.mapper;

import allcount.poc.openbankingoauth.entity.OpenBankingOAuthAccessTokenEntity;
import allcount.poc.openbankingoauth.entity.OpenBankingOAuthSessionEntity;
import allcount.poc.openbankingoauth.object.dto.OpenBankingOAuthAccessTokenResponseDto;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.object.enums.OpenBankingOAuthAccessTokenTypeEnum;
import allcount.poc.user.entity.AllcountUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

/**
 * Mapper for the OpenBankingOAuthAccessTokenEntity.
 */
@Component
public class OpenBankingOAuthAccessTokenResponseMapper {
    private static final String FIELD_ACCESS_TOKEN = "access_token";
    private static final String FIELD_REFRESH_TOKEN = "refresh_token";
    private static final String FIELD_EXPIRES_IN = "expires_in";
    private static final String FIELD_SCOPE = "scope";
    private static final String FIELD_TOKEN_TYPE = "token_type";

    /**
     * Maps the OpenBankingOAuthAccessTokenEntity to the OpenBankingOAuthAccessTokenResponseDto.
     *
     * @param openBankingOAuthAccessToken - the OpenBankingOAuthAccessTokenEntity
     * @return the OpenBankingOAuthAccessTokenResponseDto
     */
    public OpenBankingOAuthAccessTokenResponseDto mapToOpenBankingOAuthAccessTokenResponse(
            OpenBankingOAuthAccessTokenEntity openBankingOAuthAccessToken) {

        return OpenBankingOAuthAccessTokenResponseDto.builder()
                .id(openBankingOAuthAccessToken.getId())
                .userId(openBankingOAuthAccessToken.getUser().getId())
                .bank(openBankingOAuthAccessToken.getBank())
                .accessToken(openBankingOAuthAccessToken.getAccessToken())
                .refreshToken(openBankingOAuthAccessToken.getRefreshToken())
                .tokenType(openBankingOAuthAccessToken.getTokenType())
                .startDateTime(openBankingOAuthAccessToken.getStartDateTime())
                .endDateTime(openBankingOAuthAccessToken.getEndDateTime())
                .scope(openBankingOAuthAccessToken.getScope())
                .build();
    }

    /**
     * Maps the Response to the OpenBankingOAuthAccessTokenEntity.
     *
     * @param response - the Response
     * @param session  - the OpenBankingOAuthSessionEntity
     * @return the OpenBankingOAuthAccessTokenEntity
     * @throws JsonProcessingException - if the response cannot be processed
     */
    public OpenBankingOAuthAccessTokenEntity mapToOpenBankingOAuthAccessTokenEntity(
            Response response,
            OpenBankingOAuthSessionEntity session
    ) throws JsonProcessingException {
        String responseWithAccessToken = response.readEntity(String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(responseWithAccessToken);

        assert jsonNode != null;
        String accessToken = jsonNode.get(FIELD_ACCESS_TOKEN).textValue();
        String tokenType = jsonNode.get(FIELD_TOKEN_TYPE).textValue();
        long expiresIn = jsonNode.get(FIELD_EXPIRES_IN).longValue();
        String refreshToken = jsonNode.get(FIELD_REFRESH_TOKEN).textValue();
        String scope = jsonNode.get(FIELD_SCOPE).textValue();
        LocalDateTime now = LocalDateTime.now();

        return OpenBankingOAuthAccessTokenEntity.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .startDateTime(now)
                .expiresIn(expiresIn)
                .endDateTime(now.plusSeconds(expiresIn))
                .tokenType(OpenBankingOAuthAccessTokenTypeEnum.fromValue(tokenType))
                .scope(scope)
                .user(session.getUser())
                .bank(session.getBank())
                .build();
    }

    /**
     * Maps the Response to the OpenBankingOAuthAccessTokenEntity.
     *
     * @param response - the Response
     * @param session  - the OpenBankingOAuthSessionEntity
     * @return the OpenBankingOAuthAccessTokenEntity
     * @throws JsonProcessingException - if the response cannot be processed
     */
    public OpenBankingOAuthAccessTokenEntity mapToOpenBankingOAuthRefreshTokenEntity(
            Response response,
            OpenBankingBankEnum bank,
            AllcountUser user
    ) throws JsonProcessingException {
        String responseWithAccessToken = response.readEntity(String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(responseWithAccessToken);

        assert jsonNode != null;
        String accessToken = jsonNode.get(FIELD_ACCESS_TOKEN).textValue();
        String tokenType = jsonNode.get(FIELD_TOKEN_TYPE).textValue();
        long expiresIn = jsonNode.get(FIELD_EXPIRES_IN).longValue();
        String refreshToken = jsonNode.get(FIELD_REFRESH_TOKEN).textValue();
        String scope = jsonNode.get(FIELD_SCOPE).textValue();
        LocalDateTime now = LocalDateTime.now();

        return OpenBankingOAuthAccessTokenEntity.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .startDateTime(now)
                .endDateTime(now.plusSeconds(expiresIn))
                .tokenType(OpenBankingOAuthAccessTokenTypeEnum.fromValue(tokenType))
                .scope(scope)
                .user(user)
                .bank(bank)
                .build();
    }
}
