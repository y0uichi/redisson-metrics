package metrics.redis.redisson.connection;

import metrics.redis.ConnectionPoolStatistic;
import metrics.redis.impl.RedisListenerManager;
import org.redisson.connection.ClientConnectionsEntry;
import org.redisson.connection.ClusterConnectionManager;
import org.redisson.connection.ConnectionsHolder;
import org.redisson.connection.MasterSlaveEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClusterConnectionManagerHolder {

    final private HashSet<ClusterConnectionManager> clusterConnectionManagers = new HashSet<>();

    public final static ClusterConnectionManagerHolder INSTANCE = new ClusterConnectionManagerHolder();

    Logger logger = LoggerFactory.getLogger(ClusterConnectionManagerHolder.class);

    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private ClusterConnectionManagerHolder() {
        scheduler.scheduleAtFixedRate(this::connectionMonitor, 0, 1, TimeUnit.SECONDS);
    }

    public void add(ClusterConnectionManager clusterConnectionManager) {
        clusterConnectionManagers.add(clusterConnectionManager);
    }

    public void connectionMonitor() {
        clusterConnectionManagers.forEach(this::connectionMonitor);
    }

    protected void connectionMonitor(ClusterConnectionManager clusterConnectionManager) {
        Collection<MasterSlaveEntry> masterSlaveEntries = clusterConnectionManager.getEntrySet();
        if (masterSlaveEntries.isEmpty())
            return;

        List<ConnectionPoolStatistic> pool = masterSlaveEntries.stream().flatMap(this::masterPool)
            .collect(Collectors.toList());

//        pool.forEach(p -> {
//            logger.info(
//                "node {}:{} {} idle {} active {} total {}",
//                p.getAddress().getHostName(),
//                p.getAddress().getPort(),
//                p.getNodeType(),
//                p.getFreeConnectionCounter(),
//                p.getActiveConnectionCounter(),
//                p.getConnectionCounter()
//            );
//        });

        RedisListenerManager.getInstance().notifyConnectionPool(pool);
    }

    protected Stream<ConnectionPoolStatistic> masterPool(MasterSlaveEntry masterSlaveEntry) {
        Collection<ClientConnectionsEntry> connectionsEntries = masterSlaveEntry.getAllEntries();
        Stream<ConnectionPoolStatistic> slavePool = connectionsEntries.stream().map(this::slavePoolSize);
        List<ConnectionPoolStatistic> pool = slavePool.collect(Collectors.toList());
        pool.add(from(masterSlaveEntry.getEntry()));

//        logger.debug(
//            "all connections {}, free connections {}",
//            pool.stream().map(ConnectionPoolStatistic::getConnectionCounter).reduce(0, Integer::sum),
//            pool.stream().map(ConnectionPoolStatistic::getFreeConnectionCounter).reduce(0, Integer::sum)
//        );

        return pool.stream();
    }

    protected ConnectionPoolStatistic slavePoolSize(ClientConnectionsEntry entry) {
        return from(entry);
    }

    protected ConnectionPoolStatistic from(ClientConnectionsEntry entry) {
        ConnectionsHolder<?> holder = entry.getConnectionsHolder();
        InetSocketAddress address = entry.getClient().getAddr();
        int freeConnections = holder.getFreeConnections().size();
        int allConnections = holder.getAllConnections().size();

        return new ConnectionPoolStatistic(
            entry.getNodeType().toString(),
            address,
            allConnections,
            freeConnections
        );
    }
}
