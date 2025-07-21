package metrics.spring.example.domain.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class Account {

    private Long id;

    private BigDecimal balance;
}
