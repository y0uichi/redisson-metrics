package metrics.spring.example.interfaces.account.web;

import metrics.spring.example.interfaces.account.facade.AccountServiceFacade;
import metrics.spring.example.interfaces.account.facade.dto.AccountDTO;
import metrics.spring.example.interfaces.account.facade.dto.CreateBatchAccount;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class AccountController {

    protected AccountServiceFacade accountServiceFacade;

    public AccountController(AccountServiceFacade accountServiceFacade) {
        this.accountServiceFacade = accountServiceFacade;
    }

    @PostMapping
    public AccountDTO create(@RequestBody AccountDTO account) {
        return accountServiceFacade.create(account);
    }

    @PostMapping("/batch")
    public int create(@RequestBody CreateBatchAccount batch) {
        return accountServiceFacade.generateAccounts(batch.getTotal());
    }

    @GetMapping("/batch/{size}")
    public List<AccountDTO> pickAccounts(@PathVariable("size") int size) {
        return accountServiceFacade.pickAccounts(size);
    }
}
