package metrics.spring.example.infrastructures;

import metrics.spring.example.domain.model.Account;
import metrics.spring.example.domain.model.AccountRepository;
import org.redisson.api.RBucket;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountRedisRepository implements AccountRepository {

    RedissonClient client;

    public AccountRedisRepository(RedissonClient client) {
        this.client = client;
    }

    @Override
    public Account create(Account account) {

        RBucket<BigDecimal> balance = client.getBucket(String.format("balance:%d", account.getId()));
        if (balance.isExists()) {

        }

        balance.set(account.getBalance());

        RSet<Long> identities = client.getSet("accounts");
        identities.add(account.getId());

        return account;
    }
}
