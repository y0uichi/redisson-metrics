package metrics.spring.example.infrastructures;

import metrics.spring.example.domain.model.account.Account;
import metrics.spring.example.domain.model.account.AccountRepository;
import org.redisson.api.RBucket;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Override
    public Account get(Long id) {
        RSet<Long> identities =  client.getSet("accounts");
        RBucket<BigDecimal> balance = client.getBucket(String.format("balance:%d", id));
        return Account.builder().id(id).balance(balance.get()).build();
    }

    @Override
    public List<Account> pickAccounts(int size) {
        RSet<Long> identities =  client.getSet("accounts");
        Set<Long> batch = identities.random(size);
        return batch
            .stream()
            .map(id -> {
                RBucket<BigDecimal> balance = client.getBucket(String.format("balance:%d", id));
                return Account.builder().id(id).balance(balance.get()).build();
            })
            .collect(Collectors.toList());
    }

    @Override
    public int getSize() {
        return client.getSet("accounts").size();
    }
}
