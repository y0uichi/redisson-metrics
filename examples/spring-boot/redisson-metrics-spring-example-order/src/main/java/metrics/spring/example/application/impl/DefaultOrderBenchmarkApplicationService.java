package metrics.spring.example.application.impl;

import metrics.spring.example.application.OrderBenchmarkApplicationService;
import metrics.spring.example.domain.service.AccountService;
import metrics.spring.example.interfaces.account.facade.dto.AccountDTO;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DefaultOrderBenchmarkApplicationService implements OrderBenchmarkApplicationService {

    AccountService accountService;

    public DefaultOrderBenchmarkApplicationService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void execute(int taskQuantity, Duration duration) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(taskQuantity);
        executor.setMaxPoolSize(taskQuantity);
        executor.initialize();

        List<AccountDTO> accounts = accountService.pickAccounts(taskQuantity);

        for (AccountDTO account : accounts) {
            executor.submit(() -> execute(account, duration));
        }

    }

    @Override
    public void execute(AccountDTO account, Duration duration) {
        LocalDateTime now = LocalDateTime.now();

        while (true) {

            Duration between = Duration.between(now, LocalDateTime.now());
            if (between.compareTo(duration) > 0) {
                break;
            }

            execute(account);
        }
    }

    protected void execute(AccountDTO accountDTO) {
        AccountDTO account = accountService.get(accountDTO.getId());

    }
}
