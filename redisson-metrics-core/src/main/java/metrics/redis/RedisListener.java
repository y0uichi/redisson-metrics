package metrics.redis;

import java.util.List;

public interface RedisListener {

    void command(RedisCommand execution);

    void connectionPoolStatistic(List<ConnectionPoolStatistic> statistics);
}
