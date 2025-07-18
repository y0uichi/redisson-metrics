package metrics.redis.redisson.client;

import io.netty.util.AttributeKey;
import org.redisson.client.RedisClientConfig;

public class Keys {
    static public final AttributeKey<RedisClientConfig> redisClientConfigKey = AttributeKey.valueOf("redis_client_config");
}
