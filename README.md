
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

![Screenshot](./docs/screenshot.png)

# Usage

Add the dependency `spring-boot-starter-redisson-metrics` to your application.

```xml
<dependency>
    <groupId>metrics</groupId>
    <artifactId>spring-boot-starter-redisson-metrics</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```