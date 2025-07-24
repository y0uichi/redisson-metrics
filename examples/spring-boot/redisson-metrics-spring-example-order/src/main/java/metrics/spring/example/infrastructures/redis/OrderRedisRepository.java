package metrics.spring.example.infrastructures.redis;

import metrics.spring.example.domain.model.Order;
import metrics.spring.example.domain.model.OrderRepository;
import metrics.spring.example.domain.model.account.Account;
import org.redisson.api.RBucket;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class OrderRedisRepository implements OrderRepository {

    private final RedissonClient client;

    public OrderRedisRepository(RedissonClient client) {
        this.client = client;
    }


    @Override
    public Order create(Order order) {
        RBucket<Order> bucket = getOrderBucket(order.getId());
        bucket.set(order);
        getScoredSortedSet(order.getAccount().getId())
            .add(
                order.getCreatedAt().toEpochSecond(ZoneOffset.UTC),
                order.getId()
            );

        return bucket.get();
    }

    @Override
    public Order get(Long id) {
        return getOrderBucket(id).get();
    }

    @Override
    public Collection<Order> list(Long accountId) {
        return getScoredSortedSet(accountId).readAll()
            .stream()
            .map(this::get)
            .collect(Collectors.toList());
    }

    @Override
    public Order delete(Long id) {
        Order order = getOrderBucket(id).get();
        Account account = order.getAccount();
        getOrderBucket(id).delete();
        getScoredSortedSet(account.getId()).remove(id);
        return order;
    }

    protected RBucket<Order> getOrderBucket(Long id) {
        return client.getBucket("order:" + id);
    }

    protected RScoredSortedSet<Long> getScoredSortedSet(Long accountId) {
        return client.getScoredSortedSet("account_orders:" + accountId);
    }
}
