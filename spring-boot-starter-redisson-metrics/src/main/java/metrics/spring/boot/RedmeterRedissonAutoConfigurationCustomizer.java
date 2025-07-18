package metrics.spring.boot;

import metrics.redis.redisson.client.RedmeterNettyHook;
import org.redisson.config.Config;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;

public class RedmeterRedissonAutoConfigurationCustomizer implements RedissonAutoConfigurationCustomizer {

    @Override
    public void customize(Config config) {
        config.setNettyHook(new RedmeterNettyHook());
    }
}
