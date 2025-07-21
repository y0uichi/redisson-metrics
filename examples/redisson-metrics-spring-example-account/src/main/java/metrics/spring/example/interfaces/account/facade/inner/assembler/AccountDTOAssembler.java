package metrics.spring.example.interfaces.account.facade.inner.assembler;

import metrics.spring.example.domain.model.account.Account;
import metrics.spring.example.interfaces.account.facade.dto.AccountDTO;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<AccountDTO> toDTO(Collection<Account> dto) {
        return dto.stream().map(this::from).collect(Collectors.toList());
    }
}
