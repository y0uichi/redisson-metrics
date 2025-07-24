package metrics.spring.example.domain.service;

import metrics.spring.example.interfaces.account.facade.dto.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "account-service", url = "${services.account.url}")
public interface AccountService {

    @GetMapping("/batch/{size}")
//    @RequestLine("GET /batch/{size}")
    List<AccountDTO> pickAccounts(@PathVariable("size") int size);
}
