package allcount.poc.transaction.entity;

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
 * Entity for the transaction.
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
    private String currencyCode;

    @Column(name = "booking-date", nullable = false)
    @NonNull
    private Date bookingDate;

    @Column(name = "origin-iban", nullable = false)
    @ValidIbanCode
    private String originIban;

    @Override
    protected String toStringForHashOnly() {
        return getClass().getName() + super.getId();
    }
}
