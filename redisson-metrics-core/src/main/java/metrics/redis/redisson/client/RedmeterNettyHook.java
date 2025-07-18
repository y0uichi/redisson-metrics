package metrics.redis.redisson.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import metrics.redis.redisson.client.handler.ConnectionWatchDog;
import org.redisson.client.NettyHook;

public class RedmeterNettyHook implements NettyHook {
    @Override
    public void afterBoostrapInitialization(Bootstrap bootstrap) {

    }

    @Override
    public void afterChannelInitialization(Channel channel) {
        channel.pipeline().addFirst(new ConnectionWatchDog());
//        CommandEncoder encoder = channel.pipeline().get(CommandEncoder.class);
//        RedisClientConfig config = channel.attr(Keys.redisClientConfigKey).get();
//        ChannelPipeline pipeline = channel.pipeline();
//        ChannelHandler handler = channel.pipeline().replace("commandEncoder", "commandEncoder", new CommandEncoderWrapper(config.getCommandMapper()));
    }
}
