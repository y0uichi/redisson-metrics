package metrics.redis;

import java.util.List;

public class MetricsRedisListener implements RedisListener {

    RedisMetrics redisMetrics;

    public MetricsRedisListener(RedisMetrics redisMetrics) {
        this.redisMetrics = redisMetrics;
    }

    @Override
    public void command(RedisCommand execution) {

        redisMetrics.recordCommandExecution(execution.getName());
        redisMetrics.recordCommandBytesReceived(execution.getName(), execution.getReceivedSize());
        redisMetrics.recordCommandBytesSent(execution.getName(), execution.getSentSize());

        redisMetrics.recordCommandUsageMillis(execution.getName(), execution.getDuration());
    }

    @Override
    public void connectionPoolStatistic(List<ConnectionPoolStatistic> statistics) {
        this.redisMetrics.setConnectionPoolStatistic(statistics);
    }
}
