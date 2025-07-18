package metrics.redis;

import java.net.InetSocketAddress;

public class ConnectionPoolStatistic {

    final private String nodeType;

    final private InetSocketAddress address;

    final private int connectionCounter;

    final private int freeConnectionCounter;

    public ConnectionPoolStatistic(String nodeType, InetSocketAddress address, int connectionCounter, int freeConnectionCounter) {
        this.nodeType = nodeType;
        this.address = address;
        this.connectionCounter = connectionCounter;
        this.freeConnectionCounter = freeConnectionCounter;
    }

    public String getNodeType() {
        return nodeType;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public int getConnectionCounter() {
        return this.connectionCounter;
    }

    public int getFreeConnectionCounter() {
        return freeConnectionCounter;
    }

    public int getActiveConnectionCounter() {
        return connectionCounter - freeConnectionCounter;
    }
}
