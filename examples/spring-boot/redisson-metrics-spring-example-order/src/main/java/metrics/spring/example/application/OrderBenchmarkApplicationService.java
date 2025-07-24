package metrics.spring.example.application;

import metrics.spring.example.interfaces.account.facade.dto.AccountDTO;

import java.time.Duration;

public interface OrderBenchmarkApplicationService {

    void execute(int taskQuantity, Duration duration);

    void execute(AccountDTO account, Duration duration);
}
