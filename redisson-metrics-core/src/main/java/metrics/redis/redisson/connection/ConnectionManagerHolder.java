package metrics.redis.redisson.connection;

import metrics.redis.ConnectionPoolStatistic;
import org.redisson.connection.ClientConnectionsEntry;
import org.redisson.connection.ConnectionManager;
import org.redisson.connection.ConnectionsHolder;
import org.redisson.connection.MasterSlaveEntry;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ConnectionManagerHolder implements ConnectionPoolStatisticPolling {

    final private HashSet<ConnectionManager> connectionManagers = new HashSet<>();

    public final static ConnectionManagerHolder INSTANCE = new ConnectionManagerHolder();

    private ConnectionManagerHolder() {
    }

    public void add(ConnectionManager masterSlaveConnectionManager) {
        connectionManagers.add(masterSlaveConnectionManager);
    }

    @Override
    public Set<ConnectionPoolStatistic> poll() {
        return connectionManagers
            .stream()
            .map(ConnectionManager::getEntrySet)
            .filter(entrySet -> !entrySet.isEmpty())
            .flatMap(Collection::stream)
            .map(MasterSlaveEntry::getAllEntries)
            .filter(allEntries -> !allEntries.isEmpty())
            .flatMap(Collection::stream)
            .map(this::toConnectionPoolStatistic)
            .collect(Collectors.toSet());
    }

    protected ConnectionPoolStatistic toConnectionPoolStatistic(ClientConnectionsEntry entry) {
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
