package metrics.spring.example.domain.model.account;

import java.util.List;

public interface AccountRepository {

    Account create(Account account);

    List<Account> pickAccounts(int size);

    int getSize();
}
