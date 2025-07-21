package metrics.spring.example.interfaces.account.facade.inner;

import metrics.spring.example.application.AccountService;
import metrics.spring.example.domain.model.Account;
import metrics.spring.example.interfaces.account.facade.AccountServiceFacade;
import metrics.spring.example.interfaces.account.facade.dto.AccountDTO;
import metrics.spring.example.interfaces.account.facade.inner.assembler.AccountDTOAssembler;
import org.springframework.stereotype.Component;

@Component
public class AccountServiceFacadeImpl implements AccountServiceFacade {

    protected AccountService accountService;

    public AccountServiceFacadeImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public AccountDTO create(AccountDTO account) {
        AccountDTOAssembler assembler = new AccountDTOAssembler();
        Account model = assembler.toDTO(account);
        model = accountService.create(model);
        return assembler.from(model);
    }
}
