package allcount.poc.openbankingoauth.entity;

import allcount.poc.core.domain.entity.AllcountEntity;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.user.entity.AllcountUser;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

/**
 * Entity storing the OpenBanking OAuth data.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class OpenBankingOAuthEntity extends AllcountEntity {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected OpenBankingBankEnum bank;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    protected AllcountUser user;
}

