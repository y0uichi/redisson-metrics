package metrics.spring.example.domain.model.account;

public interface AccountRepository {

    Account create(Account account);

    int getSize();
}
