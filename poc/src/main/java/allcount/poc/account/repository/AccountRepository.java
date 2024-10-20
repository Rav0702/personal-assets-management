package allcount.poc.account.repository;

import allcount.poc.account.entity.AccountEntity;
import allcount.poc.user.entity.AllcountUser;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the AccountEntity.
 */
public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {

    /**
     * Find account by its IBAN.
     *
     * @param iban IBAN.
     * @return account with the given IBAN. Returns empty optional if not found.
     */
    Optional<AccountEntity> findByIban(String iban);

    /**
     * Find all accounts by owner user and account.
     *
     * @param user user.
     * @return list of accounts owned by the user.
     */
    List<AccountEntity> findAllByUser(AllcountUser user);
}
