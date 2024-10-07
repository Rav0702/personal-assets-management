package allcount.poc.user.entity;

import allcount.poc.core.domain.entity.AllcountEntity;
import allcount.poc.credential.entity.UserCredential;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "allcount_user")
public class AllcountUser extends AllcountEntity {

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "user_credential_id", referencedColumnName = "id")
    private UserCredential userCredential;

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "user_details_id", referencedColumnName = "id")
    private AllcountUserDetailsEntity userDetails;

    @Override
    protected String toStringForHashOnly() {
        return "";
    }
}
