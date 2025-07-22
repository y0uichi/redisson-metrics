# Redisson-metrics

**Redisson-metrics** は、Redissonクライアントのパフォーマンス監視に特化したオープンソースツールです。主要な実行時メトリクスを収集・表示することで、開発者がRedisアプリケーションをより深く理解し、最適化するのに役立ちます。

## 特徴

* Redissonクライアントのパフォーマンスメトリクスをリアルタイムで監視
* Grafanaとのシームレスな統合により、直感的なデータ可視化を実現
* カスタムメトリクスやアラートルールの設定に対応
* 軽量な設計で、システムパフォーマンスへの影響は最小限

## 監視対象のメトリクス

* コネクションプールの状態
* コマンド実行の遅延
* 処理スループット
* エラー率の統計

![Screenshot](./docs/screenshot.png)

# 使用方法

アプリケーションに `spring-boot-starter-redisson-metrics` の依存関係を追加します。

```xml
<dependency>
    <groupId>metrics</groupId>
    <artifactId>spring-boot-starter-redisson-metrics</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```