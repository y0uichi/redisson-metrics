package metrics.spring.boot;

import io.micrometer.core.instrument.MeterRegistry;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import metrics.redis.redisson.RedissonInterceptor;
import metrics.redis.redisson.connection.ConnectionPoolStatisticPolling;
import metrics.redis.redisson.connection.DefaultClusterConnectionManagerHolder;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(ApplicationConfig.class)
@Configuration
@AutoConfigureBefore(RedissonAutoConfiguration.class)
public class RedmeterAutoConfiguration {

    @Bean
    public RedissonAutoConfigurationCustomizer redissonAutoConfigurationCustomizer() {
        return new RedmeterRedissonAutoConfigurationCustomizer();
    }

    @Bean
    public ServerStartedListener serverStartedListener(MeterRegistry meterRegistry, ApplicationConfig applicationConfig, ConnectionPoolStatisticPolling polling) {
        return new ServerStartedListener(meterRegistry, applicationConfig, polling);
    }

    @Bean
    public ConnectionPoolStatisticPolling connectionPoolStatisticPolling() {
        return DefaultClusterConnectionManagerHolder.INSTANCE;
    }

    @Bean
    public BeanFactoryPostProcessor redissonInitProcessor() {
        return new BeanFactoryPostProcessor() {
            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
                try {
                    initRedmeter();
                } catch (NotFoundException | CannotCompileException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    public void initRedmeter() throws NotFoundException, CannotCompileException {
        RedissonInterceptor.init();
    }
}
