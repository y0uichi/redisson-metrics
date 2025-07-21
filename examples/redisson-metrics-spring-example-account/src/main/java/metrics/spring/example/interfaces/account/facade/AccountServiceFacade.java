package metrics.spring.example.interfaces.account.facade;

import metrics.spring.example.interfaces.account.facade.dto.AccountDTO;

public interface AccountServiceFacade {

    AccountDTO create(AccountDTO account);

    int generateAccounts(int total);
}
