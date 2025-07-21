package metrics.spring.example.interfaces.account.web;

import metrics.spring.example.interfaces.account.facade.AccountServiceFacade;
import metrics.spring.example.interfaces.account.facade.dto.AccountDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
