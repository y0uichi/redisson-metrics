package metrics.spring.example.domain.model.id;

public interface IdRepository {

    Long nextId(String key);
}
