package metrics.spring.boot;

import io.micrometer.core.instrument.MeterRegistry;
import metrics.redis.DefaultRedisMetrics;
import metrics.redis.MetricsRedisListener;
import metrics.redis.RedisMetrics;
import metrics.redis.impl.RedisListenerManager;
import metrics.redis.redisson.connection.ConnectionPoolStatisticPolling;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ServerStartedListener implements ApplicationListener<WebServerInitializedEvent> {

    MeterRegistry meterRegistry;

    ApplicationConfig applicationConfig;

    ConnectionPoolStatisticPolling poolStatisticPolling;

    public ServerStartedListener(MeterRegistry meterRegistry, ApplicationConfig applicationConfig, ConnectionPoolStatisticPolling poolStatisticPolling) {
        this.meterRegistry = meterRegistry;
        this.applicationConfig = applicationConfig;
        this.poolStatisticPolling = poolStatisticPolling;
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        applicationConfig.setPort(event.getWebServer().getPort());
        RedisMetrics redisMetrics = new DefaultRedisMetrics(
            meterRegistry,
            new TagSet(applicationConfig),
            poolStatisticPolling
        );
        MetricsRedisListener metricsRedisListener = new MetricsRedisListener(redisMetrics);

        RedisListenerManager.getInstance().addListener(metricsRedisListener);
    }
}
