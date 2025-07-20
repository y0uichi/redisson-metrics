package metrics.redis;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import metrics.redis.redisson.connection.ConnectionPoolStatisticPolling;

import java.time.Duration;
import java.util.Set;

public class DefaultRedisMetrics extends AbstractRedisMetrics implements RedisMetrics {

    public DefaultRedisMetrics(MeterRegistry registry, Set<Tag> tags, ConnectionPoolStatisticPolling polling) {
        super(registry, tags, polling);
    }

    @Override
    public void recordCommandExecution(String command) {
        getCommandCounter(command).increment();
    }

    @Override
    public void recordCommandUsageMillis(String command, Duration usage) {
        getCommandUsage(command).record(usage);
    }

    @Override
    public void recordCommandBytesSent(String command, long size) {
        getCommandSize(command, "send").record(size);
    }

    @Override
    public void recordCommandBytesReceived(String command, long size) {
        getCommandSize(command, "receive").record(size);
    }
}
