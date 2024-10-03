package allcount.poc.credential.object;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Password {
    private final transient String passwordValue;

    public Password(final String password) {
        this.passwordValue = password;
    }

    @Override
    public String toString() {
        return passwordValue;
    }
}
