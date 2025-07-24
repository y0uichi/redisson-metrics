package metrics.spring.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/experiment/measure")
public class ExperimentMeasureController {

    @Autowired
    ApplicationContext context;

    @Autowired
    @Qualifier("taskExecutor1")
    private ThreadPoolTaskExecutor taskExecutor1;

    @PostMapping
    void launch(@RequestParam int tasks, @RequestParam Duration duration) {
        taskExecutor1.submit(() -> {
            for (int i = 0; i < tasks  ; i++) {

                int finalIndex = i;
                taskExecutor1.submit(() -> {
                    ExperimentMeasure experimentMeasure = context.getBean(ExperimentMeasure.class);
                    experimentMeasure.launch(finalIndex, duration);
                });
            }
        });
    }
}
