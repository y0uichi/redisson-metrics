
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

```bash
docker exec -it redis-master-1 redis-cli --cluster create \
redis-master-1:6379 \
redis-master-2:6379 \
redis-master-3:6379 \
redis-slave-1:6379 \
redis-slave-2:6379 \
redis-slave-3:6379 \
redis-slave-4:6379 \
redis-slave-5:6379 \
redis-slave-6:6379 \
redis-slave-7:6379 \
redis-slave-8:6379 \
redis-slave-9:6379 \
--cluster-replicas 3
```