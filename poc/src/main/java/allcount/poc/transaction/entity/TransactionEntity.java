package allcount.poc.transaction.entity;

import allcount.poc.account.entity.AccountEntity;
import allcount.poc.core.domain.entity.AllcountEntity;
import allcount.poc.shared.annotation.ValidISO4217Alpha3Code;
import allcount.poc.shared.annotation.ValidIbanCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

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
