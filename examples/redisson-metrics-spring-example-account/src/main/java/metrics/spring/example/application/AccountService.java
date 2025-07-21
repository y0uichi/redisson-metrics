package metrics.spring.example.application;

import metrics.spring.example.domain.model.account.Account;

public interface AccountService {

    Account create(Account account);

    Account get(Long id);

    int generateAccounts(int total);
}
