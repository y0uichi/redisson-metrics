package metrics.redis;

import io.micrometer.core.instrument.*;
import metrics.infra.ListenerIP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractRedisMetrics {

    public final static String METRIC_NAME_PREFIX = "metrics";

    public final static String METRIC_NAME_ACTIVE_CONNECTIONS = METRIC_NAME_PREFIX + ".redis.connections.active";

    public final static String METRIC_NAME_IDLE_CONNECTIONS = METRIC_NAME_PREFIX + ".redis.connections.idle";

    public final static String METRIC_NAME_TOTAL_CONNECTIONS = METRIC_NAME_PREFIX + ".redis.connections.total";

    public final static String METRIC_NAME_COMMAND_SIZE = METRIC_NAME_PREFIX + ".redis.command.size";

    protected final MeterRegistry registry;

    protected final Gauge activeConnectionGauge;

    protected final Gauge totalConnectionGauge;

    protected final Gauge idleConnectionGauge;

    protected final Map<String, DistributionSummary> commandSize = new ConcurrentHashMap<>();

    final Map<String, Counter> commandCounters = new ConcurrentHashMap<>();

    final Map<String, Timer> commandUsages = new ConcurrentHashMap<>();

    List<ConnectionPoolStatistic> poolStatistics = new ArrayList<>();

    final Set<Tag> tags;

    Logger logger = LoggerFactory.getLogger(AbstractRedisMetrics.class);

    public AbstractRedisMetrics(MeterRegistry registry, Set<Tag> tags) {
        this.registry = registry;
        this.tags = processTags(tags);

        activeConnectionGauge = Gauge.builder(
            METRIC_NAME_ACTIVE_CONNECTIONS,
            this,
            AbstractRedisMetrics::getActiveConnections
        )
            .tags(tags)
            .register(registry);

        totalConnectionGauge = Gauge.builder(
            METRIC_NAME_TOTAL_CONNECTIONS,
            this,
            AbstractRedisMetrics::getTotalConnections
        )
            .tags(tags)
            .register(registry);

        idleConnectionGauge = Gauge.builder(
            METRIC_NAME_IDLE_CONNECTIONS,
            this,
            AbstractRedisMetrics::getIdleConnections
        )
            .tags(tags)
            .register(registry);
    }

    protected Set<Tag> processTags(Set<Tag> tags) {
        if (tags.stream().noneMatch(tag -> tag.getKey().equals("node")))
            tags.add(Tag.of("node", ListenerIP.INSTANCE.toString()));
        return tags;
    }

    protected int getActiveConnections() {
        int actives = this.poolStatistics
            .stream()
            .map(ConnectionPoolStatistic::getActiveConnectionCounter)
            .reduce(0, Integer::sum);

        logger.info("active connections {}", actives);

        return actives;
    }

    protected int getTotalConnections() {
        return this.poolStatistics
            .stream()
            .map(ConnectionPoolStatistic::getConnectionCounter)
            .reduce(0, Integer::sum);
    }

    protected int getIdleConnections() {
        return this.poolStatistics
            .stream()
            .map(ConnectionPoolStatistic::getFreeConnectionCounter)
            .reduce(0, Integer::sum);
    }

    protected Counter getCommandCounter(String commandName) {
        return commandCounters.computeIfAbsent(commandName, name ->
            Counter.builder("redis.command.counter")
                .description("test")
                .tag("command", name)
                .tags(tags)
                .register(registry)
        );
    }

    protected Timer getCommandUsage(String commandName) {
        return commandUsages.computeIfAbsent(commandName, name ->
            Timer.builder("redis.command.timer")
                .description("test")
                .publishPercentiles(0.5, 0.75, 0.95, 0.99)
                .publishPercentileHistogram()
                .tag("command", name)
                .tags(tags)
                .register(registry)
        );
    }

    protected DistributionSummary getCommandSize(String commandName, String type) {
        String key = commandName + type;
        return commandSize.computeIfAbsent(key, _key ->
            DistributionSummary
                .builder(METRIC_NAME_COMMAND_SIZE)
                .tag("ip", ListenerIP.INSTANCE.toString())
                .tag("command", commandName)
                .tag("type", type)
                .publishPercentileHistogram()
                .serviceLevelObjectives(50, 95, 99)
                .baseUnit("bytes")
                .register(registry)
        );
    }

    public void setConnectionPoolStatistic(List<ConnectionPoolStatistic> connectionPoolStatistics) {
        this.poolStatistics = connectionPoolStatistics;
    }
}
