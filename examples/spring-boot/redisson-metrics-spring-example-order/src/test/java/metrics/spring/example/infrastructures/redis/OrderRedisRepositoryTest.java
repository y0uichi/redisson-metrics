package metrics.spring.example.infrastructures.redis;

import metrics.spring.example.domain.model.Order;
import metrics.spring.example.domain.model.account.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class OrderRedisRepositoryTest {

    @Autowired
    OrderRedisRepository repository;

    @Test
    public void testAllMethods() {

        Account account = Account
            .builder()
            .id(new Random().nextLong())
            .build();

        Order order1 = Order
            .builder()
            .id(new Random().nextLong())
            .account(account)
            .amount(BigDecimal.TEN)
            .createdAt(LocalDateTime.now())
            .build();

        Order order2 = Order
            .builder()
            .id(new Random().nextLong())
            .account(account)
            .amount(BigDecimal.TEN)
            .createdAt(LocalDateTime.now())
            .build();

        repository.create(order1);
        repository.create(order2);

        Order gotOrder1 = repository.get(order1.getId());
        Order gotOrder2 = repository.get(order2.getId());

        Collection<Order> orders = repository.list(account.getId());

        repository.delete(gotOrder1.getId());
        repository.delete(gotOrder2.getId());

        orders = repository.list(account.getId());
    }

}
