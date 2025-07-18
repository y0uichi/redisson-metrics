package metrics.redis;

import java.time.Duration;

public interface RedisCommand {

    String getName();

    Object[] getParameters();

    Duration getDuration();

    long getSentSize();

    long getReceivedSize();
}
