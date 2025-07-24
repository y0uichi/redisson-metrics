package metrics.spring.example.interfaces.account.facade;

import metrics.spring.example.interfaces.account.facade.dto.AccountDTO;

import java.util.List;

public interface AccountServiceFacade {

    AccountDTO create(AccountDTO account);

    AccountDTO get(Long id);

    int generateAccounts(int total);

    List<AccountDTO> pickAccounts(int size);
}
