package allcount.poc.transaction.mapper;

import allcount.poc.transaction.entity.TransactionEntity;
import allcount.poc.transaction.object.dto.TransactionDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting Transaction Entity to DTO.
 */
@Component
public class TransactionEntityToDtoMapper {

    /**
     * Maps the list of entities to the DTOs.
     *
     * @param transactionEntities List of entities to map.
     *  @return List of DTOs.
     */
    public List<TransactionDto> mapToDtos(List<TransactionEntity> transactionEntities) {
        return transactionEntities.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    /**
     * Maps the entity to the DTO.
     *
     * @param transactionEntity Entity to map.
     * @return DTO.
     */
    private TransactionDto mapToDto(TransactionEntity transactionEntity) {
        return TransactionDto.builder()
                .amount(transactionEntity.getAmount())
                .currencyCode(transactionEntity.getCurrencyCode())
                .bookingDate(transactionEntity.getBookingDate())
                .valueDate(transactionEntity.getValueDate())
                .originIban(transactionEntity.getOriginAccount().getIban())
                .counterPartyName(transactionEntity.getCounterPartyName())
                .counterPartyIban(transactionEntity.getCounterPartyIban())
                .originBankId(transactionEntity.getExternalBankingId().getExternalId())
                .build();
    }
}
