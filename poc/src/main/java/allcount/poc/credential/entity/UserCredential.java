package allcount.poc.credential.entity;

import allcount.poc.core.domain.entity.AllcountEntity;
import allcount.poc.credential.converter.HashedPasswordAttributeConverter;
import allcount.poc.credential.object.HashedPassword;
import allcount.poc.user.entity.AllcountUser;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_credential")
public class UserCredential extends AllcountEntity implements UserDetails {

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}
