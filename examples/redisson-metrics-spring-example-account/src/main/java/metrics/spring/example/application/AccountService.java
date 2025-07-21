package metrics.spring.example.application;

import metrics.spring.example.domain.model.account.Account;

import java.util.List;

public interface AccountService {

    Account create(Account account);

    Account get(Long id);

    int generateAccounts(int total);

    List<Account> pickAccounts(int size);
}
