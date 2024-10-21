package allcount.poc.cashaccount.mapper;

import allcount.poc.cashaccount.entity.CashAccountEntity;
import allcount.poc.cashaccount.object.dto.CashAccountResponseDto;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.user.entity.AllcountUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Mapper for the AccountResponse.
 */
@Component
public class CashAccountResponseMapper {
    private static final String ACCOUNT_TYPE = "accountType";
    private static final String BIC = "bic";
    private static final String CURRENCY_CODE = "currencyCode";
    private static final String CURRENT_BALANCE = "currentBalance";
    private static final String IBAN = "iban";
    private static final String PRODUCT_DESCRIPTION = "productDescription";

    /**
     * Map the account to the response.
     *
     * @param cashAccountEntity the account
     * @return the response
     */
    public CashAccountResponseDto mapToAccountResponse(CashAccountEntity cashAccountEntity) {
        return CashAccountResponseDto.builder()
                .bank(cashAccountEntity.getBank())
                .accountType(cashAccountEntity.getAccountType())
                .bic(cashAccountEntity.getBic())
                .currencyCode(cashAccountEntity.getCurrencyCode())
                .currentBalance(cashAccountEntity.getCurrentBalance())
                .iban(cashAccountEntity.getIban())
                .productDescription(cashAccountEntity.getProductDescription())
                .build();
    }

    /**
     * Map the response to the account entities.
     *
     * @param response the response
     * @param user the user
     * @param bank the bank
     * @return the account entities
     * @throws JsonProcessingException if there is an error
     */
    public List<CashAccountEntity> mapToAccountEntities(Response response, AllcountUser user, OpenBankingBankEnum bank)
            throws JsonProcessingException {
        String responseWithAccessToken = response.readEntity(String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(responseWithAccessToken);

        JsonNode accountsArray = jsonNode.get("accounts");
        List<CashAccountEntity> accountEntities = new ArrayList<>();

        if (accountsArray != null && accountsArray.isArray()) {
            for (JsonNode accountNode : accountsArray) {
                String accountType = accountNode.get(ACCOUNT_TYPE).textValue();
                String bic = accountNode.has(BIC) ? accountNode.get(BIC).textValue() : null;
                String currencyCode = accountNode.get(CURRENCY_CODE).textValue();
                BigDecimal currentBalance = accountNode.get(CURRENT_BALANCE).decimalValue();
                String iban = accountNode.get(IBAN).textValue();
                String productDescription = accountNode.get(PRODUCT_DESCRIPTION).textValue();

                CashAccountEntity cashAccountEntity = CashAccountEntity.builder()
                        .bank(bank)
                        .user(user)
                        .accountType(accountType)
                        .bic(bic)
                        .currencyCode(currencyCode)
                        .currentBalance(currentBalance)
                        .iban(iban)
                        .productDescription(productDescription)
                        .build();

                accountEntities.add(cashAccountEntity);
            }
        }

        return accountEntities;
    }
}
