package metrics.redis.redisson.connection;

import metrics.redis.ConnectionPoolStatistic;
import org.redisson.connection.ClientConnectionsEntry;
import org.redisson.connection.ClusterConnectionManager;
import org.redisson.connection.ConnectionsHolder;
import org.redisson.connection.MasterSlaveEntry;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultClusterConnectionManagerHolder implements ConnectionPoolStatisticPolling {

    final private HashSet<ClusterConnectionManager> clusterConnectionManagers = new HashSet<>();

    public final static DefaultClusterConnectionManagerHolder INSTANCE = new DefaultClusterConnectionManagerHolder();

    private DefaultClusterConnectionManagerHolder() {
    }

    public void add(ClusterConnectionManager clusterConnectionManager) {
        clusterConnectionManagers.add(clusterConnectionManager);
    }

    protected Stream<ConnectionPoolStatistic> connectionMonitor(Collection<MasterSlaveEntry> masterSlaveEntries) {
        return masterSlaveEntries
            .stream()
            .flatMap(this::masterPool);
    }

    protected Stream<ConnectionPoolStatistic> masterPool(MasterSlaveEntry masterSlaveEntry) {
        Collection<ClientConnectionsEntry> connectionsEntries = masterSlaveEntry.getAllEntries();
        return connectionsEntries.stream().map(this::slavePoolSize);
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

    @Override
    public Set<ConnectionPoolStatistic> poll() {
        return clusterConnectionManagers
            .stream()
            .map(ClusterConnectionManager::getEntrySet)
            .filter(entrySet -> !entrySet.isEmpty())
            .flatMap(this::connectionMonitor)
            .collect(Collectors.toSet());


//        List<ConnectionPoolStatistic> pool = masterSlaveEntries.stream().flatMap(this::masterPool)
//                .collect(Collectors.toList());
    }
}
