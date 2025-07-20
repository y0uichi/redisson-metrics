package metrics.redis.impl;

import metrics.redis.ConnectionPoolStatistic;
import metrics.redis.RedisCommand;
import metrics.redis.RedisListener;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class RedisListenerManager {

    private static final RedisListenerManager INSTANCE = new RedisListenerManager();

    private final Set<RedisListener> listeners = new CopyOnWriteArraySet<>();

    private RedisListenerManager() { }

    public static RedisListenerManager getInstance() {
        return INSTANCE;
    }

    public void addListener(RedisListener listener) {
        listeners.add(listener);
    }

    public Set<RedisListener> getListeners() {
        return listeners;
    }

    public void removeListener(RedisListener listener) {
        listeners.remove(listener);
    }

    public void notifyCommandExecuted(RedisCommand command) {
        for (RedisListener listener: listeners) {
            listener.command(command);
        }
    }
}
