package metrics.spring.example.application.impl;

import lombok.extern.slf4j.Slf4j;
import metrics.spring.example.application.AccountService;
import metrics.spring.example.domain.model.account.Account;
import metrics.spring.example.domain.model.account.AccountRepository;
import metrics.spring.example.domain.model.id.IdRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AccountServiceImpl implements AccountService {

    AccountRepository accountRepository;

    IdRepository idRepository;

    public AccountServiceImpl(AccountRepository accountRepository, IdRepository idRepository) {
        this.accountRepository = accountRepository;
        this.idRepository = idRepository;
    }

    @Override
    public Account create(Account account) {
        return accountRepository.create(account);
    }

    @Override
    public Account get(Long id) {
        return null;
    }

    public int generateAccounts(int total) {
        int allAccounts = accountRepository.getSize();

        if (total < allAccounts) {
            return 0;
        }
        int size = total - allAccounts;
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            10, 20, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100)
        );
        CountDownLatch latch = new CountDownLatch(size);

        for (int i = 0; i < total - allAccounts; i++) {
            long id = idRepository.nextId("account");

            executor.submit(() -> {
                Account account = Account
                    .builder()
                    .id(id)
                    .balance(BigDecimal.valueOf(Long.MAX_VALUE))
                    .build();
                accountRepository.create(account);
                latch.countDown();

                if (latch.getCount() % 10 == 0) {
                    log.debug("count down value {}", latch.getCount());
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            return (int)latch.getCount();
        }

        return total - allAccounts;
    }

    @Override
    public List<Account> pickAccounts(int size) {
        return accountRepository.pickAccounts(size);
    }
}
