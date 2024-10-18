package allcount.poc.openbanking.entity;

import allcount.poc.core.domain.entity.AllcountEntity;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing an Entity ID from an external banking system.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "external_banking_id",
        uniqueConstraints = @UniqueConstraint(columnNames = {"bank", "external-id"}
        ))
public class ExternalBankingIdEntity extends AllcountEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "bank", nullable = false)
    @NonNull
    private OpenBankingBankEnum bank;

    @Column(name = "external_id", nullable = false)
    @NonNull
    private String externalId;

    @Override
    protected String toStringForHashOnly() {
        return "ExternalBankingIdEntity:" + super.getId() + ":" + bank + ":" + externalId;
    }
}
