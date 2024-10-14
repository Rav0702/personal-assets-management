package allcount.poc.account.mapper;

import allcount.poc.account.entity.AccountEntity;
import allcount.poc.account.object.dto.AccountResponseDto;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.user.entity.AllcountUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class AccountResponseMapper {
    private static final String ACCOUNT_TYPE = "accountType";
    private static final String BIC = "bic";
    private static final String CURRENCY_CODE = "currencyCode";
    private static final String CURRENT_BALANCE = "currentBalance";
    private static final String IBAN = "iban";
    private static final String PRODUCT_DESCRIPTION = "productDescription";

    public AccountResponseDto mapToAccountResponse(AccountEntity accountEntity) {
        return AccountResponseDto.builder()
                .bank(accountEntity.getBank())
                .accountType(accountEntity.getAccountType())
                .bic(accountEntity.getBic())
                .currencyCode(accountEntity.getCurrencyCode())
                .currentBalance(accountEntity.getCurrentBalance())
                .iban(accountEntity.getIban())
                .productDescription(accountEntity.getProductDescription())
                .build();
    }

    public List<AccountEntity> mapToAccountEntities(Response response, AllcountUser user, OpenBankingBankEnum bank) throws JsonProcessingException {
        String responseWithAccessToken = response.readEntity(String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(responseWithAccessToken);

        JsonNode accountsArray = jsonNode.get("accounts");
        List<AccountEntity> accountEntities = new ArrayList<>();

        if (accountsArray != null && accountsArray.isArray()) {
            for (JsonNode accountNode : accountsArray) {
                String accountType = accountNode.get(ACCOUNT_TYPE).textValue();
                String bic = accountNode.has(BIC) ? accountNode.get(BIC).textValue() : null;
                String currencyCode = accountNode.get(CURRENCY_CODE).textValue();
                BigDecimal currentBalance = accountNode.get(CURRENT_BALANCE).decimalValue();
                String iban = accountNode.get(IBAN).textValue();
                String productDescription = accountNode.get(PRODUCT_DESCRIPTION).textValue();

                AccountEntity accountEntity = AccountEntity.builder()
                        .bank(bank)
                        .user(user)
                        .accountType(accountType)
                        .bic(bic)
                        .currencyCode(currencyCode)
                        .currentBalance(currentBalance)
                        .iban(iban)
                        .productDescription(productDescription)
                        .build();

                accountEntities.add(accountEntity);
            }
        }

        return accountEntities;
    }
}
