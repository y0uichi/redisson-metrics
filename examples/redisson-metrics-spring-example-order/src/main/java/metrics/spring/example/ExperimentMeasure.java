package metrics.spring.example;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@Scope("prototype")
@Component
class ExperimentMeasure {

    @Autowired
    RedissonClient client;


    public void launch(int index) {
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

        log.debug("Thread {} launch 1 time.", Thread.currentThread().getName());

//        log.debug("counted {}:{}, duration {}"
//            , batchIndex
//            , index
//            , System.currentTimeMillis() - time1
//        );
    }

    public void launch(int index, Duration duration) {
        LocalDateTime now = LocalDateTime.now();

        // Time's up
        while (Duration.between(LocalDateTime.now(), now).compareTo(duration) < 0) {
            launch(index);
        }
    }
}