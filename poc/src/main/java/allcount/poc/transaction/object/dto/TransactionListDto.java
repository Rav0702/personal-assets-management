package allcount.poc.transaction.object.dto;

import allcount.poc.core.domain.object.dto.AllcountDto;
import java.util.List;
import lombok.*;


/**
 * DTO for the TransactionList.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionListDto extends AllcountDto {
    private List<TransactionDto> transactions;
}
