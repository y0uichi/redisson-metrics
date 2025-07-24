package metrics.spring.example.infrastructures;

import metrics.spring.example.domain.model.id.IdRepository;
import org.redisson.api.RIdGenerator;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
public class IdRedisRepository implements IdRepository {

    protected RedissonClient client;

    public IdRedisRepository(RedissonClient client) {
        this.client = client;
    }

    @Override
    public Long nextId(String key) {

        RIdGenerator idGenerator = getIdGenerator(key);
        idGenerator.tryInit(1, 500);

        return idGenerator.nextId();
    }

    protected RIdGenerator getIdGenerator(String key) {
        return client.getIdGenerator(String.format("%s_id", key));
    }
}
