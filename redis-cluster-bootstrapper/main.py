import subprocess
import time
from flask import Flask, Response

app = Flask(__name__)

REDIS_NODES = [
    "redis-master-1:6379",
    "redis-master-2:6379",
    "redis-master-3:6379",
    "redis-slave-1:6379",
    "redis-slave-2:6379",
    "redis-slave-3:6379",
    "redis-slave-4:6379",
    "redis-slave-5:6379",
    "redis-slave-6:6379",
    "redis-slave-7:6379",
    "redis-slave-8:6379",
    "redis-slave-9:6379",
]

CLUSTER_REPLICAS = "3"


def is_cluster_ok():
    try:
        # Use redis-cli directly to check cluster info
        result = subprocess.run(
            ["redis-cli", "-h", "redis-master-1", "-p", "6379", "cluster", "info"],
            capture_output=True, text=True, timeout=5
        )
        return "cluster_state:ok" in result.stdout
    except Exception as e:
        print(f"Check cluster error: {e}")
        return False

def create_cluster():
    # Use redis-cli directly to create cluster
    cmd = [
        "redis-cli", "--cluster", "create",
        *REDIS_NODES,
        "--cluster-replicas", CLUSTER_REPLICAS
    ]
    proc = subprocess.Popen(cmd, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
    try:
        out, err = proc.communicate(input="yes\n", timeout=60)
        print(out)
        print(err)
    except subprocess.TimeoutExpired:
        proc.kill()
        print("Cluster creation timed out")

@app.route("/")
def health():
    if is_cluster_ok():
        return Response("OK", status=200)
    else:
        return Response("Cluster not ready", status=503)

if __name__ == "__main__":
    if not is_cluster_ok():
        print("Cluster not found, creating...")
        create_cluster()
        for _ in range(30):
            if is_cluster_ok():
                print("Cluster is ready!")
                break
            time.sleep(2)
        else:
            print("Cluster creation failed or timed out.")
            exit(1)
    else:
        print("Cluster already exists.")

    app.run(host="0.0.0.0", port=80) 