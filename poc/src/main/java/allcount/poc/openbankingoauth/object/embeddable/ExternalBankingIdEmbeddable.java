package allcount.poc.openbankingoauth.object.embeddable;

import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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
