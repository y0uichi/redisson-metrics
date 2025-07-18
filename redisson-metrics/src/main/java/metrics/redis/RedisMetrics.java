package metrics.redis;

import java.time.Duration;
import java.util.List;

public interface RedisMetrics {

    void recordCommandExecution(String command);

    void recordCommandUsageMillis(String command, Duration usage);

    void recordCommandBytesSent(String command, long size);

    void recordCommandBytesReceived(String command, long size);

    int recordConnectionActive();

    int recordConnectionTotal();

    int recordConnectionIdle();

    void setConnectionPoolStatistic(List<ConnectionPoolStatistic> connectionPoolStatistics);
}
