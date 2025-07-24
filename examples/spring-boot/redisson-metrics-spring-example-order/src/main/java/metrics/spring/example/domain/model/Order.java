package metrics.spring.example.domain.model;

import lombok.Builder;
import lombok.Data;
import metrics.spring.example.domain.model.account.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
public class Order {

    private Long id;

    private BigDecimal amount;

    private Account account;

    private LocalDateTime createdAt;
}
