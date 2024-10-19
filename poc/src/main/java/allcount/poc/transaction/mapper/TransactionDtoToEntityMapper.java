package allcount.poc.transaction.mapper;

import allcount.poc.account.entity.AccountEntity;
import allcount.poc.openbanking.entity.ExternalBankingIdEntity;
import allcount.poc.transaction.entity.TransactionEntity;
import allcount.poc.transaction.object.dto.TransactionDto;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting Transaction DTO to an entity.
 */
@Component
public class TransactionDtoToEntityMapper {

    /**
     * Maps the DTO to the entity.
     *
     * @param transactionDtoList List of DTOs to map
     * @param accounts           List of accounts to resolve the IBANs
     */
    public List<TransactionEntity> mapToEntities(List<TransactionDto> transactionDtoList, List<AccountEntity> accounts) {
        Map<String, AccountEntity> ibanToAccountMap = accounts.stream().collect(Collectors.toMap(AccountEntity::getIban, account -> account));
        return transactionDtoList.stream().map(transactionDto -> mapToEntity(transactionDto, ibanToAccountMap)).collect(Collectors.toList());
    }

    /**
     * Maps the DTO to the entity.
     *
     * @param transactionDto   DTO to map
     * @param ibanToAccountMap Map of IBANs to accounts to resolve the IBAN in the transaction DTO
     */
    public TransactionEntity mapToEntity(TransactionDto transactionDto, Map<String, AccountEntity> ibanToAccountMap) {
        return TransactionEntity.builder()
                .amount(transactionDto.getAmount())
                .currencyCode(transactionDto.getCurrencyCode())
                .bookingDate(transactionDto.getBookingDate())
                .valueDate(transactionDto.getValueDate())
                .originAccount(ibanToAccountMap.get(transactionDto.getOriginIban()))
                .counterPartyName(transactionDto.getCounterPartyName())
                .counterPartyIban(transactionDto.getCounterPartyIban())
                .counterPartyName(transactionDto.getCounterPartyName())
                .externalBankingIds(List.of(
                        ExternalBankingIdEntity.builder()
                                .externalId(String.valueOf(transactionDto.getId()))
                                .bank(ibanToAccountMap.get(transactionDto.getOriginIban()).getBank())
                                .build()))
                .build();
    }

}
