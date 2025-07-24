package metrics.redis;

public interface RedisListener {

    void command(RedisCommand execution);
}
