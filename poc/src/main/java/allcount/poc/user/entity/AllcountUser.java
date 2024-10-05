package allcount.poc.user.entity;

import allcount.poc.core.domain.entity.AllcountEntity;
import allcount.poc.credential.entity.UserCredential;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity for the AllcountUser.
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "allcount_user")
public class AllcountUser extends AllcountEntity {

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "user_credential_id", referencedColumnName = "id")
    private UserCredential userCredential;

    /**
     * Returns a string representation of the object for hashing purposes.
     *
     * @return a string representation of the object for hashing purposes.
     */
    @Override
    protected String toStringForHashOnly() {
        return "";
    }
}
