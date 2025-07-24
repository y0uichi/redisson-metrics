package metrics.spring.example.application.impl;

import lombok.extern.slf4j.Slf4j;
import metrics.spring.example.application.OrderBenchmarkApplicationService;
import metrics.spring.example.domain.model.Order;
import metrics.spring.example.domain.model.OrderRepository;
import metrics.spring.example.domain.model.account.Account;
import metrics.spring.example.domain.service.AccountService;
import metrics.spring.example.interfaces.account.facade.dto.AccountDTO;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
public class DefaultOrderBenchmarkApplicationService implements OrderBenchmarkApplicationService {

    AccountService accountService;

    OrderRepository orderRepository;

    public DefaultOrderBenchmarkApplicationService(
        AccountService accountService,
        OrderRepository orderRepository) {
        this.accountService = accountService;
        this.orderRepository = orderRepository;
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
        LocalDateTime startedAt = LocalDateTime.now();

        while (true) {

            Duration between = Duration.between(startedAt, LocalDateTime.now());
            if (between.compareTo(duration) > 0) {
                break;
            }

            execute(account);
        }
    }

    protected void execute(AccountDTO accountDTO) {
        AccountDTO account = accountService.get(accountDTO.getId());

        for (int i = 0; i < 10; i++) {
            accountService.get(accountDTO.getId());
        }

        for (int i = 0; i < 100; i++) {
            Order order = Order
                .builder()
                    .id(new Random().nextLong())
                    .amount(BigDecimal.TEN)
                    .createdAt(LocalDateTime.now())
                    .account(
                        Account
                            .builder()
                            .id(account.getId())
                            .balance(account.getBalance())
                            .build()
                    )
                .build();
            orderRepository.create(order);
            orderRepository.get(order.getId());
            Collection<Order> orders = orderRepository.list(accountDTO.getId());

            log.debug("create order {}", order.getId());
        }

        Collection<Order> orders = orderRepository.list(accountDTO.getId());
        orders
            .stream()
            .map(Order::getId)
            .forEach(orderRepository::delete);

        log.debug("task complete.");
    }
}
