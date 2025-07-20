#!/bin/bash

set -e

echo "🔨 Building all modules with Maven..."
mvn clean package -DskipTests

modules=(
  "order:examples/redisson-metrics-spring-example-order"
  "account:examples/redisson-metrics-spring-example-account"
)

DOCKERFILE=Dockerfile

# 循环构建每个模块镜像
for entry in "${modules[@]}"
do
  # 分割名称和路径
  IFS=":" read -r image_name module_path <<< "$entry"

  # 查找 JAR 文件（匹配优先包含 SNAPSHOT）
  jar_file=$(find "$module_path/target" -maxdepth 1 -type f -name "*.jar" | grep SNAPSHOT || find "$module_path/target" -maxdepth 1 -type f -name "*.jar" | head -n 1)

  if [[ -f "$jar_file" ]]; then
    echo "📦 Building Docker image: $image_name (from $jar_file)"
    docker build -f "$DOCKERFILE" \
      --build-arg JAR_FILE="$jar_file" \
      -t "$image_name:latest" .
  else
    echo "❌ JAR not found for module at $module_path"
  fi
done

echo "✅ All modules built successfully."