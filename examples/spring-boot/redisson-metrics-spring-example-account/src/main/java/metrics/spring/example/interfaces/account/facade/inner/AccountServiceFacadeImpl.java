package metrics.spring.example.interfaces.account.facade.inner;

import metrics.spring.example.application.AccountService;
import metrics.spring.example.domain.model.account.Account;
import metrics.spring.example.interfaces.account.facade.AccountServiceFacade;
import metrics.spring.example.interfaces.account.facade.dto.AccountDTO;
import metrics.spring.example.interfaces.account.facade.inner.assembler.AccountDTOAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

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

    @Override
    public AccountDTO get(Long id) {
        AccountDTOAssembler assembler = new AccountDTOAssembler();
        return assembler.from(accountService.get(id));
    }

    @Override
    public int generateAccounts(int total) {
        return accountService.generateAccounts(total);
    }

    @Override
    public List<AccountDTO> pickAccounts(int size) {
        AccountDTOAssembler assembler = new AccountDTOAssembler();
        return assembler.toDTO(accountService.pickAccounts(size));
    }
}
