package metrics.redis.redisson;

import metrics.redis.RedisCommand;
import metrics.redis.impl.RedisListenerManager;
import org.redisson.client.protocol.CommandData;
import org.redisson.command.RedisExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings({"rawtypes"})
public class RedissonCommand implements RedisCommand {

    RedisExecutor executor;

    final LocalDateTime startedAt = LocalDateTime.now();

    long sentSize = -1;

    long receivedSize = -1;

    LocalDateTime endedAt = LocalDateTime.MIN;

    CommandData<?, ?> commandData;

    final Logger logger = LoggerFactory.getLogger(RedissonCommand.class);

    final static Class<RedisExecutor> redisExecutorClass;

    final static Method getMainPromiseMethod;

//    final static Method getCommand;

    final static Method getParams;

    static {
        redisExecutorClass = RedisExecutor.class;
        try {
            getMainPromiseMethod = redisExecutorClass.getMethod("getMainPromise");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

//        try {
//            getCommand = redisExecutorClass.getMethod("getCommand");
//        } catch (NoSuchMethodException e) {
//            throw new RuntimeException(e);
//        }

        try {
            getParams = redisExecutorClass.getMethod("getParams");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public RedissonCommand() {
    }

    protected void waitCommand() {
        try {
            Object future = getMainPromiseMethod.invoke(executor);
            waitCommand((CompletableFuture)future);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

//    protected void waitCommandResponseCompleted() {
//        try {
//            org.redisson.client.protocol.RedisCommand<?> command = (org.redisson.client.protocol.RedisCommand<?>)getCommand.invoke(executor);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        } catch (InvocationTargetException e) {
//            throw new RuntimeException(e);
//        }
//    }

    protected void waitCommand(CompletableFuture<?> future) {
        future.whenComplete(this::commandWhenComplete);
    }

    protected void commandWhenComplete(Object result, Object ex) {
        endedAt = LocalDateTime.now();

        // RedisListenerManager.getInstance().notifyCommandExecuted(this);
    }

    protected void commandWhenComplete(Object result, Exception ex) {

    }

    @Override
    public String getName() {
        StringBuilder sb = new StringBuilder();
        org.redisson.client.protocol.RedisCommand<?> command = commandData.getCommand();
        sb.append(command.getName());
        if (command.getSubName() == null || command.getSubName().isEmpty()) {
            return sb.toString();
        }

        sb.append(" ");
        sb.append(command.getSubName());
        return sb.toString();
    }

    @Override
    public Object[] getParameters() {
        return new Object[0];
    }

    @Override
    public Duration getDuration() {
        return Duration.between(startedAt, endedAt);
    }

    public void setSentSize(long size) {
        this.sentSize = size;

        logger.debug("command {} sent {} bytes", getName(), size);
    }

    public long getSentSize() {
        return this.sentSize;
    }

    public void setReceivedSize(long size) {
        this.receivedSize = size;

        logger.debug("command {} received {} bytes", getName(), size);
    }

    public long getReceivedSize() {
        return this.receivedSize;
    }

    public <V, R> void setRedisExecutor(RedisExecutor<V, R> redisExecutor) {
        this.executor = redisExecutor;
        waitCommand();
    }

    public RedisExecutor getRedisExecutor() {
        return this.executor;
    }

    public void setCommandData(CommandData<?, ?> commandData) {
        this.commandData = commandData;
    }

    public CommandData<?, ?> getCommandData() {
        return commandData;
    }

    public void start(CommandData<?, ?> commandData, int sentSize) {
        this.commandData = commandData;
        this.sentSize = sentSize;
    }

    public void finish(int receivedSize) {
        this.receivedSize = receivedSize;
        endedAt = LocalDateTime.now();

        RedisListenerManager.getInstance().notifyCommandExecuted(this);
    }
}
