package metrics;

import java.util.LinkedHashMap;

public class ObjectPool {
    public static final ObjectPool INSTANCE = new ObjectPool();

    private final LinkedHashMap<Object, Object> objects = new LinkedHashMap<>();

    public boolean containsKey(Object object) {
        return objects.containsKey(object);
    }

    public void add(Object object, Object context) {
        objects.put(object, context);
    }

    @SuppressWarnings({"unchecked"})
    public <T> T remove(Object object) {
        return (T)objects.remove(object);
    }

    @SuppressWarnings({"unchecked"})
    public <T> T get(Object object) {
        return (T)objects.get(object);
    }
}
