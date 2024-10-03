package allcount.poc.credential.entity;

import allcount.poc.core.domain.entity.AllcountEntity;
import allcount.poc.credential.converter.HashedPasswordAttributeConverter;
import allcount.poc.credential.object.HashedPassword;
import allcount.poc.user.entity.AllcountUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_credential")
public class UserCredential extends AllcountEntity {

    @Column
    private String username;

    @Column(name = "password")
    @Convert(converter = HashedPasswordAttributeConverter.class)
    private HashedPassword password;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AllcountUser user;


    @Override
    protected byte[] calculateEntityHash() {
        return new byte[0];
    }
}
