#!/bin/bash

# 服务启动脚本 - 解决多副本端口冲突问题

set -e

echo "🚀 启动服务配置选择器"
echo "请选择启动模式："
echo "1) 生产模式 - 使用 Nginx 负载均衡器 (推荐)"
echo "2) 开发模式 - 移除端口映射，仅内部网络"
echo "3) 动态端口模式 - 使用端口范围映射"
echo "4) 单副本模式 - 仅用于调试"

read -p "请输入选择 (1-4): " choice

case $choice in
    1)
        echo "📦 启动生产模式 (Nginx 负载均衡)"
        docker-compose up -d
        echo "✅ 服务已启动"
        echo "🌐 访问地址:"
        echo "   - Order API: http://localhost:8080"
        echo "   - Prometheus: http://localhost:9090"
        echo "   - Grafana: http://localhost:3000"
        ;;
    2)
        echo "🔧 启动开发模式 (仅内部网络)"
        # 移除 nginx 服务
        docker-compose up -d --scale order=3 --scale account=3
        echo "✅ 服务已启动 (仅内部网络访问)"
        echo "📊 监控地址:"
        echo "   - Prometheus: http://localhost:9090"
        echo "   - Grafana: http://localhost:3000"
        ;;
    3)
        echo "🔄 启动动态端口模式"
        docker-compose -f docker-compose-dynamic-ports.yml up -d
        echo "✅ 服务已启动"
        echo "🌐 访问地址:"
        echo "   - Order API: http://localhost:8080-8082 (随机分配)"
        echo "   - Prometheus: http://localhost:9090"
        echo "   - Grafana: http://localhost:3000"
        ;;
    4)
        echo "🐛 启动单副本调试模式"
        docker-compose up -d --scale order=1 --scale account=1
        echo "✅ 服务已启动 (单副本)"
        echo "🌐 访问地址:"
        echo "   - Order API: http://localhost:8080"
        echo "   - Prometheus: http://localhost:9090"
        echo "   - Grafana: http://localhost:3000"
        ;;
    *)
        echo "❌ 无效选择"
        exit 1
        ;;
esac

echo ""
echo "📋 查看服务状态:"
docker-compose ps 