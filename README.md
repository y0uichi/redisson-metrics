
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