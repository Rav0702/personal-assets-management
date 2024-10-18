package allcount.poc.transaction.entity;

import allcount.poc.core.domain.entity.AllcountEntity;
import allcount.poc.openbanking.entity.ExternalBankingIdEntity;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.shared.annotation.ValidIBANCode;
import allcount.poc.shared.annotation.ValidISO4217Alpha3Code;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Javadoc todo.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction")
public class TransactionEntity extends AllcountEntity {
    @Column(name = "amount", precision = 15, scale = 2, nullable = false)
    @NonNull
    private BigDecimal amount;

    @Column(name = "currency", length = 3, nullable = false)
    @ValidISO4217Alpha3Code
    @NonNull
    private String currencyCode;

    @Column(name = "booking_date", nullable = false)
    @NonNull
    private LocalDate bookingDate;

    @Column(name = "value_date")
    @Nullable
    private LocalDate valueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "origin_bank")
    @Nullable
    private OpenBankingBankEnum originBank;

    @Column(name = "origin_iban", nullable = false)
    @ValidIBANCode
    @NonNull
    private String originIban;

    @Column(name = "counter_party_name")
    @Nullable
    private String counterPartyName;

    // todo: add iban validation (allow null)
    @Column(name = "counter_party_iban")
    @Nullable
    private String counterPartyIban;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "external_transaction_id")
    private List<ExternalBankingIdEntity> externalBankingIds = new ArrayList<>();

    @Override
    protected String toStringForHashOnly() {
        return getClass().getName() + super.getId();
    }
}
