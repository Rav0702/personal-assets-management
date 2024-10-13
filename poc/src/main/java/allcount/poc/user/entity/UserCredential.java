package allcount.poc.user.entity;

import allcount.poc.core.domain.entity.AllcountEntity;
import allcount.poc.user.converter.HashedPasswordAttributeConverter;
import allcount.poc.user.object.HashedPassword;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Entity for the user credential.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_credential")
public class UserCredential extends AllcountEntity {

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password")
    @Convert(converter = HashedPasswordAttributeConverter.class)
    @JsonIgnore
    private HashedPassword password;

    @OneToOne(mappedBy = "userCredential", optional = false)
    private AllcountUser user;

    /**
     * String representation of the object for hashing only.
     *
     * @return String representation of the object for hashing only.
     */
    @Override
    protected String toStringForHashOnly() {
        return this.user.getId() + this.username;
    }
}
