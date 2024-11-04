package allcount.poc.transaction.entity;

import allcount.poc.cashaccount.entity.CashAccountEntity;
import allcount.poc.core.domain.entity.AllcountEntity;
import allcount.poc.openbankingoauth.object.embeddable.ExternalBankingIdEmbeddable;
import allcount.poc.shared.annotation.ValidCurrencyCode;
import allcount.poc.shared.annotation.ValidIbanCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.lang.Nullable;

/**
 * Entity representing a Bank Transaction.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "transaction",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"bank", "external_id"}),
        },
        indexes = {
            @Index(columnList = "bank, external_id"),
        }
)
public class TransactionEntity extends AllcountEntity {
    @Column(name = "amount", precision = 15, scale = 2, nullable = false)
    @NonNull
    private BigDecimal amount;

    @Column(name = "currency", length = 3, nullable = false)
    @ValidCurrencyCode
    @NonNull
    private String currencyCode;

    @Column(name = "booking_date", nullable = false)
    @NonNull
    private LocalDate bookingDate;

    @Column(name = "value_date")
    @Nullable
    private LocalDate valueDate;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_account_id")
    @NonNull
    private CashAccountEntity originAccount;

    @Column(name = "counter_party_name")
    @Nullable
    private String counterPartyName;

    @Column(name = "counter_party_iban")
    @ValidIbanCode
    private String counterPartyIban;

    @Embedded
    @NonNull
    private ExternalBankingIdEmbeddable externalBankingId;

    @Override
    protected String toStringForHashOnly() {
        return getClass().getName() + super.getId();
    }
}
