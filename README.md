
# Redisson-metrics

**Redisson-metrics** is an open-source tool focused on monitoring the performance of the Redisson client. By collecting and displaying key runtime metrics, it helps developers better understand and optimize their Redis applications.

## Features

* Real-time monitoring of Redisson client performance metrics
* Seamless integration with Grafana for intuitive data visualization
* Supports custom monitoring metrics and alert rules
* Lightweight design with minimal impact on system performance

## Monitored Metrics

* Connection pool status
* Command execution latency
* Operation throughput
* Error rate statistics


```bash
docker exec -it redis-master-1 redis-cli --cluster create \
host.docker.internal:6379 \
host.docker.internal:6380 \
host.docker.internal:6381 \
host.docker.internal:6382 \
host.docker.internal:6383 \
host.docker.internal:6384 \
host.docker.internal:6385 \
host.docker.internal:6386 \
host.docker.internal:6387 \
host.docker.internal:6388 \
host.docker.internal:6389 \
host.docker.internal:6390 \
--cluster-replicas 3
```