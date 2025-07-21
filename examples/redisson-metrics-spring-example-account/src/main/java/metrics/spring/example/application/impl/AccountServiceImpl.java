package metrics.spring.example.application.impl;

import metrics.spring.example.application.AccountService;
import metrics.spring.example.domain.model.Account;
import metrics.spring.example.domain.model.AccountRepository;
import org.springframework.stereotype.Component;

@Component
public class AccountServiceImpl implements AccountService {

    AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account create(Account account) {
        return accountRepository.create(account);
    }

    @Override
    public Account get(Long id) {
        return null;
    }
}
