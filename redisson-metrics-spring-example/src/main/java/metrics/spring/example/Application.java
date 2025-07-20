package metrics.spring.example;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Scope("prototype")
@Component
class ExperimentMeasureBuilder {
    @Autowired
    RedissonClient client;

    public void build(int batchIndex, int index) {
        long time1 = System.currentTimeMillis();

        RBucket<Integer> counterBucket = client.getBucket(String.format("counter_%d", index));
        boolean isExists = counterBucket.isExists();


        if (!isExists) {
            counterBucket.set(1);
        } else {
            counterBucket.set(counterBucket.get() + 1);
        }

        int getIndex = -1;
        for (int j = 0, size = new Random().nextInt(100); j < size; j++) {
            getIndex = counterBucket.get();
        }

//        log.debug("counted {}:{}, duration {}"
//            , batchIndex
//            , index
//            , System.currentTimeMillis() - time1
//        );
    }
}

@Slf4j
@EnableScheduling
@SpringBootApplication
public class Application {

    @Autowired
    ApplicationContext context;

    @Autowired
    @Qualifier("taskExecutor1")
    private ThreadPoolTaskExecutor taskExecutor1;

    private int batchIndex = 0;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Scheduled(fixedRate = 1000)
    public void onApplicationReady() throws InterruptedException {
        submitTaskToThreadPool();
        batchIndex++;
    }

    void submitTaskToThreadPool() {
        taskExecutor1.submit(() -> {
            for (int i = 0; i < 10  ; i++) {

                int finalIndex = i;
                taskExecutor1.submit(() -> {
                    ExperimentMeasureBuilder experimentMeasureBuilder = context.getBean(ExperimentMeasureBuilder.class);
                    experimentMeasureBuilder.build(batchIndex, finalIndex);
                });
            }
        });
    }

    void submitTaskToThread() {
        for (int i = 0; i < 1; i++) {
            int finalIndex = i;

            Thread thread = new Thread(() -> {
                ExperimentMeasureBuilder experimentMeasureBuilder = context.getBean(ExperimentMeasureBuilder.class);
                experimentMeasureBuilder.build(batchIndex, finalIndex);
            });
            thread.start();
        }
    }

    @Scheduled(fixedRate = 1000)
    public void monitorThreadPool() {
        log.debug("Thread pool status - Active: {}, Pool size: {}, Queue size: {}",
            taskExecutor1.getActiveCount(),
            taskExecutor1.getPoolSize(),
            taskExecutor1.getQueueSize()
        );
    }
}
