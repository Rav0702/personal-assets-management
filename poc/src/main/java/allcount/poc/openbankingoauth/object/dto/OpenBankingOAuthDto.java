package allcount.poc.openbankingoauth.object.dto;

import allcount.poc.core.domain.object.dto.AllcountDto;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for the OpenBankingOAuth.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OpenBankingOAuthDto extends AllcountDto {
    protected UUID userId;
    protected OpenBankingBankEnum bank;

    /**
     * Constructor.
     *
     * @param id - the id
     * @param userId - the userId
     * @param bank - the bank
     */
    public OpenBankingOAuthDto(UUID id, UUID userId, OpenBankingBankEnum bank) {
        super(id);
        this.userId = userId;
        this.bank = bank;
    }
}
