package metrics.spring.boot;

import io.micrometer.core.instrument.Tag;

import java.util.HashSet;

public class TagSet extends HashSet<Tag> {

    public TagSet(ApplicationConfig applicationConfig) {
        this.add(Tag.of("app", applicationConfig.getName()));
        this.add(
            Tag.of(
                "node",
                String.format("%s:%d", applicationConfig.getHost(), applicationConfig.getPort())
            )
        );
    }
}
