package metrics.spring.example.interfaces.benchmark.web;

import metrics.spring.example.application.OrderBenchmarkApplicationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/benchmark")
public class BenchmarkController {

    private final OrderBenchmarkApplicationService orderBenchmarkApplicationService;

    public BenchmarkController(OrderBenchmarkApplicationService orderBenchmarkApplicationService) {
        this.orderBenchmarkApplicationService = orderBenchmarkApplicationService;
    }

    @PostMapping
    public void execute(@RequestParam int size, @RequestParam Duration duration) {
        orderBenchmarkApplicationService.execute(size, duration);
    }
}
