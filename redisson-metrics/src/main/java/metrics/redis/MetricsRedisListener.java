package metrics.redis;

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
}
