package metrics.spring.example.interfaces.account.facade.inner.assembler;

import metrics.spring.example.domain.model.account.Account;
import metrics.spring.example.interfaces.account.facade.dto.AccountDTO;

public class AccountDTOAssembler {

    public AccountDTO from(Account account) {

        return AccountDTO
            .builder()
            .id(account.getId())
            .balance(account.getBalance())
            .build();
    }

    public Account toDTO(AccountDTO dto) {
        return Account
            .builder()
            .id(dto.getId())
            .balance(dto.getBalance())
            .build();
    }
}
