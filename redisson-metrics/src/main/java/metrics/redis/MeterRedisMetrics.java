package metrics.redis;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Set;

public class MeterRedisMetrics extends AbstractRedisMetrics implements RedisMetrics {

    Logger logger = LoggerFactory.getLogger(MeterRedisMetrics.class);

    public MeterRedisMetrics(MeterRegistry registry, Set<Tag> tags) {
        super(registry, tags);
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

    @Override
    public int recordConnectionActive() {
        return getActiveConnections();
    }

    @Override
    public int recordConnectionTotal() {
        return getTotalConnections();
    }

    @Override
    public int recordConnectionIdle() {
        return getIdleConnections();
    }
}
