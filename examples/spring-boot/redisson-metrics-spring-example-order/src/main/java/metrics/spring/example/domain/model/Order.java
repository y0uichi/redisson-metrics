package metrics.spring.example.domain.model;

import lombok.Data;
import metrics.spring.example.domain.model.account.Account;

@Data
public class Order {

    private Long id;

    private Account account;
}
