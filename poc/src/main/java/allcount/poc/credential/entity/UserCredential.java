package allcount.poc.credential.entity;

import allcount.poc.core.domain.entity.AllcountEntity;
import allcount.poc.credential.converter.HashedPasswordAttributeConverter;
import allcount.poc.credential.object.HashedPassword;
import allcount.poc.user.entity.AllcountUser;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_credential")
public class UserCredential extends AllcountEntity {

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password")
    @Convert(converter = HashedPasswordAttributeConverter.class)
    private HashedPassword password;

    @OneToOne(mappedBy = "userCredential", optional = false)
    private AllcountUser user;

    @Override
    protected String toStringForHashOnly() {
        return this.user.getId() + this.username;
    }
}
