package metrics.spring.boot;

import io.micrometer.core.instrument.MeterRegistry;
import metrics.redis.MeterRedisMetrics;
import metrics.redis.MetricsRedisListener;
import metrics.redis.RedisMetrics;
import metrics.redis.impl.RedisListenerManager;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ServerStartedListener implements ApplicationListener<WebServerInitializedEvent> {

    MeterRegistry meterRegistry;

    ApplicationConfig applicationConfig;

    public ServerStartedListener(MeterRegistry meterRegistry, ApplicationConfig applicationConfig) {
        this.meterRegistry = meterRegistry;
        this.applicationConfig = applicationConfig;
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        applicationConfig.setPort(event.getWebServer().getPort());
        RedisMetrics redisMetrics = new MeterRedisMetrics(meterRegistry, new TagSet(applicationConfig));
        MetricsRedisListener metricsRedisListener = new MetricsRedisListener(redisMetrics);

        RedisListenerManager.getInstance().addListener(metricsRedisListener);
    }
}
