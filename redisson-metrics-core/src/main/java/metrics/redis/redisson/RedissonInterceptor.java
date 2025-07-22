package metrics.redis.redisson;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import javassist.*;
import metrics.ObjectPool;
import metrics.redis.redisson.client.Keys;
import metrics.redis.redisson.connection.ConnectionManagerHolder;
import org.redisson.client.RedisClientConfig;
import org.redisson.client.RedisConnection;
import org.redisson.client.protocol.CommandData;
import org.redisson.command.RedisExecutor;
import org.redisson.connection.ClusterConnectionManager;
import org.redisson.connection.MasterSlaveConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RedissonInterceptor {

    static Logger logger = LoggerFactory.getLogger(RedissonInterceptor.class);

    ClassPool cp;

    static final ObjectPool pool = ObjectPool.INSTANCE;

    static final RedissonInterceptor INSTANCE = new RedissonInterceptor();

    private RedissonInterceptor() {
        cp = ClassPool.getDefault();
        cp.insertClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
    }

    protected void interceptCommandDecoder() throws NotFoundException, CannotCompileException {

        CtClass cc = cp.get("org.redisson.client.handler.CommandDecoder");
        CtMethod m = cc.getDeclaredMethod("decode");

        m.insertBefore("metrics.redis.redisson.RedissonInterceptor.commandDecodeBefore(ctx, in, out);");
        m.insertAfter("metrics.redis.redisson.RedissonInterceptor.commandDecodeAfter(ctx, in, out);");
        cc.toClass();
    }

    protected void interceptRedisExecutor() throws NotFoundException, CannotCompileException {
        CtClass cls = cp.get("org.redisson.command.RedisExecutor");
        CtMethod m = cls.getDeclaredMethod("execute");
        m.insertBefore("metrics.redis.redisson.RedissonInterceptor.redisExecutorExecute(this);");

        CtMethod handleResult = cls.getDeclaredMethod("handleResult");
        handleResult.insertBefore("metrics.redis.redisson.RedissonInterceptor.redisExecutorHandleResult(this, connectionFuture);");

        CtMethod sendCommand = cls.getDeclaredMethod("sendCommand");
        handleResult.insertBefore("metrics.redis.redisson.RedissonInterceptor.redisExecutorSendCommand(this, connectionFuture);");


        cls.addMethod(CtNewMethod.make("public java.util.concurrent.CompletableFuture getMainPromise() {" +
                "   return mainPromise;" +
                "}", cls));

        cls.addMethod(CtNewMethod.make("public org.redisson.client.protocol.RedisCommand getCommand() {" +
                "   return command;" +
                "}", cls));

        cls.addMethod(CtNewMethod.make("public Object[] getParams() {" +
                "   return params;" +
                "}", cls));

        cls.toClass();
    }

//    protected void interceptConnection() throws NotFoundException, CannotCompileException {
//        CtClass cls = cp.get("org.redisson.connection.pool.ConnectionPool");
//        CtMethod m = cls.getDeclaredMethod("acquireConnection");
//
//        m.insertAfter("metrics.redis.redisson.RedissonInterceptor.acquireConnect(this, command, entry, $_);");
//        cls.toClass();
//    }

    protected void interceptRedisChannelInitializer()
        throws NotFoundException, CannotCompileException {

        CtClass cls = cp.get("org.redisson.client.handler.RedisChannelInitializer");
        CtMethod m = cls.getDeclaredMethod("initChannel");

        m.insertBefore("metrics.redis.redisson.RedissonInterceptor.initChannel(config, ch);");
        cls.toClass();
    }

    protected void interceptCommandEnter()
            throws NotFoundException, CannotCompileException {

        CtClass cls = cp.get("org.redisson.client.handler.CommandEncoder");
        CtMethod m = cls.getDeclaredMethod("encode");

        m.insertBefore("metrics.redis.redisson.RedissonInterceptor.commandEncoderEncodeBefore(ctx, msg, out);");
        m.insertAfter("metrics.redis.redisson.RedissonInterceptor.commandEncoderEncodeAfter(ctx, msg, out);");
        cls.toClass();
    }

    protected void interceptClusterConnectionManager()
            throws NotFoundException, CannotCompileException {

        CtClass cls = cp.get("org.redisson.connection.ClusterConnectionManager");

        for (CtConstructor constructor : cls.getConstructors()) {
            constructor.insertAfter("metrics.redis.redisson.RedissonInterceptor.clusterConnectionManager(this);");
        }

        CtClass masterSlaveConnectionManagerClass = cp.get("org.redisson.connection.SingleConnectionManager");

        for (CtConstructor constructor : masterSlaveConnectionManagerClass.getConstructors()) {
            constructor.insertAfter("metrics.redis.redisson.RedissonInterceptor.masterSlaveConnectionManager(this);");
        }

        cls.toClass();
        masterSlaveConnectionManagerClass.toClass();
    }

    public static void commandEncoderEncodeBefore(ChannelHandlerContext ctx, CommandData<?, ?> msg, ByteBuf out) {
    }

    public static void commandEncoderEncodeAfter(ChannelHandlerContext ctx, CommandData<?, ?> msg, ByteBuf out) {
        int writerIndex = out.writerIndex();
        Channel channel =  ctx.channel();
        RedissonCommand command = new RedissonCommand();
        command.start(msg, writerIndex);

        pool.add(channel, command);
    }

    public static void initChannel(RedisClientConfig config, Channel channel) {
        channel.attr(Keys.redisClientConfigKey).set(config);
    }

    public static void commandDecodeBefore(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    }

    public static void commandDecodeAfter(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Channel channel = ctx.channel();

        if (!pool.containsKey(channel)) {
            return;
        }

        RedissonCommand command = pool.remove(channel);
        command.finish(in.writerIndex());
    }

    public static <V, R> void redisExecutorExecute(RedisExecutor<V, R> executor) {
    }

    public static <V, R> void redisExecutorSendCommand(RedisExecutor<V, R> executor, CompletableFuture<RedisConnection> connectionFuture) {
    }

    public static <V, R> void redisExecutorHandleResult(RedisExecutor<V, R> executor, CompletableFuture<RedisConnection> connectionFuture) {
    }

    public static void clusterConnectionManager(ClusterConnectionManager clusterConnectionManager) {
        ConnectionManagerHolder.INSTANCE.add(clusterConnectionManager);
    }

    public static void masterSlaveConnectionManager(MasterSlaveConnectionManager clusterConnectionManager) {
        ConnectionManagerHolder.INSTANCE.add(clusterConnectionManager);
    }

    public static void init() throws NotFoundException, CannotCompileException {
        try {
            INSTANCE.interceptCommandDecoder();
            INSTANCE.interceptRedisExecutor();
//        INSTANCE.interceptConnection();
            INSTANCE.interceptRedisChannelInitializer();
            INSTANCE.interceptCommandEnter();
            INSTANCE.interceptClusterConnectionManager();
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }
}
