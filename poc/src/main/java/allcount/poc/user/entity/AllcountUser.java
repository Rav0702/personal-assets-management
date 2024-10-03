package allcount.poc.user.entity;

import allcount.poc.core.domain.entity.AllcountEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "allcount_user")
public class AllcountUser extends AllcountEntity {

    @Override
    protected byte[] calculateEntityHash() {
        return new byte[0];
    }
}
