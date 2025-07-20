package metrics.redis.redisson.connection;

import metrics.redis.ConnectionPoolStatistic;

import java.util.Set;

public interface ConnectionPoolStatisticPolling {

    Set<ConnectionPoolStatistic> poll();
}
