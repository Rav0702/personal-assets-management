package allcount.poc.user.entity;

import allcount.poc.core.domain.entity.AllcountEntity;
import allcount.poc.openbankingoauth.entity.OpenBankingOAuthAccessTokenEntity;
import allcount.poc.openbankingoauth.entity.OpenBankingOAuthSessionEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Entity for the AllcountUser.
 */
@Entity
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "allcount_user")
public class AllcountUser extends AllcountEntity implements UserDetails {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "user_credential_id", referencedColumnName = "id")
    private UserCredential userCredential;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "user_details_id", referencedColumnName = "id")
    private AllcountUserDetailsEntity userDetails;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private Set<OpenBankingOAuthSessionEntity> openBankingOAuthSessions;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private Set<OpenBankingOAuthAccessTokenEntity> openBankingOAuthAccessTokens;

    /**
     * Returns a string representation of the object for hashing purposes.
     *
     * @return a string representation of the object for hashing purposes.
     */
    @Override
    protected String toStringForHashOnly() {
        return "";
    }

    /**
     * Returns the authorities of the user.
     *
     * @return the authorities of the user.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    /**
     * Returns the password of the user.
     *
     * @return the password of the user.
     */
    @Override
    public String getPassword() {
        return getUserCredential().getPassword().toString();
    }

    /**
     * Returns the username of the user.
     *
     * @return the username of the user.
     */
    @Override
    public String getUsername() {
        return getUserCredential().getUsername();
    }

    /**
     * Returns whether the account is not expired.
     *
     * @return whether the account is not expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Returns whether the account is not locked.
     *
     * @return whether the account is not locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Returns whether the credentials are not expired.
     *
     * @return whether the credentials are not expired.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Returns whether the user is enabled.
     *
     * @return whether the user is enabled.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
