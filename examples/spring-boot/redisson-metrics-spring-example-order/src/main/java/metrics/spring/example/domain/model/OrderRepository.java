package metrics.spring.example.domain.model;

import java.util.Collection;

public interface OrderRepository {

    Order create(Order order);

    Order get(Long id);

    Collection<Order> list(Long accountId);

    Order delete(Long id);
}
