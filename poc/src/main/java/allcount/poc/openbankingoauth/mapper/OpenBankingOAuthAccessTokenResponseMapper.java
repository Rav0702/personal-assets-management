package allcount.poc.openbankingoauth.mapper;

import allcount.poc.openbankingoauth.entity.OpenBankingOAuthAccessTokenRedisEntity;
import allcount.poc.openbankingoauth.entity.OpenBankingRefreshTokenEntity;
import allcount.poc.openbankingoauth.object.dto.OpenBankingOAuthAccessTokenResponseDto;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.object.enums.OpenBankingOAuthAccessTokenTypeEnum;
import allcount.poc.user.entity.AllcountUser;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.NonNull;
import org.springframework.stereotype.Component;

/**
 * Mapper for the OpenBankingRefreshTokenEntity.
 */
@Component
public class OpenBankingOAuthAccessTokenResponseMapper {
    private static final String FIELD_ACCESS_TOKEN = "access_token";
    private static final String FIELD_REFRESH_TOKEN = "refresh_token";
    private static final String FIELD_EXPIRES_IN = "expires_in";
    private static final String FIELD_SCOPE = "scope";
    private static final String FIELD_TOKEN_TYPE = "token_type";

    /**
     * Maps the OpenBankingRefreshTokenEntity to the OpenBankingOAuthAccessTokenResponseDto.
     *
     * @param openBankingOAuthAccessToken - the OpenBankingRefreshTokenEntity
     * @return the OpenBankingOAuthAccessTokenResponseDto
     */
    public OpenBankingOAuthAccessTokenResponseDto mapToOpenBankingOAuthAccessTokenResponse(
            OpenBankingRefreshTokenEntity openBankingOAuthAccessToken) {

        return OpenBankingOAuthAccessTokenResponseDto.builder()
                .id(openBankingOAuthAccessToken.getId())
                .userId(openBankingOAuthAccessToken.getUser().getId())
                .bank(openBankingOAuthAccessToken.getBank())
                .refreshToken(openBankingOAuthAccessToken.getRefreshToken())
                .tokenType(openBankingOAuthAccessToken.getTokenType())
                .scope(openBankingOAuthAccessToken.getScope())
                .build();
    }

    /**
     * Maps the Response to the OpenBankingRefreshTokenEntity.
     *
     * @param jsonNode - the jsonNode
     * @param user     - the user
     * @param bank     - the bank
     * @return the OpenBankingRefreshTokenEntity
     */
    public OpenBankingRefreshTokenEntity mapResponseToOAuthRefreshToken(
            @NonNull JsonNode jsonNode,
            AllcountUser user,
            OpenBankingBankEnum bank
    ) {
        String tokenType = jsonNode.get(FIELD_TOKEN_TYPE).textValue();
        String refreshToken = jsonNode.get(FIELD_REFRESH_TOKEN).textValue();
        String scope = jsonNode.get(FIELD_SCOPE).textValue();

        return OpenBankingRefreshTokenEntity.builder()
                .refreshToken(refreshToken)
                .tokenType(OpenBankingOAuthAccessTokenTypeEnum.fromValue(tokenType))
                .scope(scope)
                .user(user)
                .bank(bank)
                .build();
    }

    /**
     * Maps the Response to the OpenBankingOAuthAccessTokenRedisEntity.
     *
     * @param jsonNode     - the jsonNode
     * @param refreshToken - the OpenBankingRefreshTokenEntity
     * @return the OpenBankingOAuthAccessTokenRedisEntity
     */
    public OpenBankingOAuthAccessTokenRedisEntity mapResponseToOAuthAccessToken(
            @NonNull JsonNode jsonNode,
            OpenBankingRefreshTokenEntity refreshToken
    ) {
        String accessToken = jsonNode.get(FIELD_ACCESS_TOKEN).textValue();
        Long expiresIn = jsonNode.get(FIELD_EXPIRES_IN).longValue();
        String tokenType = jsonNode.get(FIELD_TOKEN_TYPE).textValue();

        return new OpenBankingOAuthAccessTokenRedisEntity(
                accessToken,
                OpenBankingOAuthAccessTokenTypeEnum.fromValue(tokenType),
                refreshToken.getUser().getId(),
                refreshToken.getBank(),
                expiresIn
        );
    }

}
