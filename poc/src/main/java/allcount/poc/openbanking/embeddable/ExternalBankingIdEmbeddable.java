package allcount.poc.openbanking.embeddable;

import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import jakarta.persistence.*;
import lombok.*;

/**
 * Embeddable representing an Entity ID from an external banking system. <br>
 * <b>It should be treated as a value object.</b>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ExternalBankingIdEmbeddable {
    @Enumerated(EnumType.STRING)
    @Column(name = "bank", nullable = false)
    @NonNull
    private OpenBankingBankEnum bank;

    @Column(name = "external_id", nullable = false)
    @NonNull
    private String externalId;
}
