package allcount.poc.transaction.entity;

import allcount.poc.account.entity.AccountEntity;
import allcount.poc.core.domain.entity.AllcountEntity;
import allcount.poc.openbanking.embeddable.ExternalBankingIdEmbeddable;
import allcount.poc.shared.annotation.ValidCurrencyCode;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.*;
import org.springframework.lang.NonNull;
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
            @UniqueConstraint(columnNames = {"bank", "id_from_bank"}),
        },
        indexes = {
            @Index(columnList = "bank, id_from_bank"),
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
    private AccountEntity originAccount;

    @Column(name = "counter_party_name")
    @Nullable
    private String counterPartyName;

    // todo: add iban validation (allow null)
    @Column(name = "counter_party_iban")
    @Nullable
    private String counterPartyIban;

    @Embedded
    @NonNull
    private ExternalBankingIdEmbeddable externalBankingId;

    @Override
    protected String toStringForHashOnly() {
        return getClass().getName() + super.getId();
    }
}
