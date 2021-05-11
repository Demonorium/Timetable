package em.demonorium.timetable.Utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import em.demonorium.timetable.Utils.VariableSystem.Variable;
import em.demonorium.timetable.Utils.VariableSystem.VariableSystem;

public class ObjectRW<T> implements Serializable {
    private TreeMap<String, Factory> registered = new TreeMap<>();

    public <K> ObjectRW<T> field(String name, Factory<K> def) {
        registered.put(name, def);
        return this;
    }

    public <K> ObjectRW<T> field(String name, K def) {
        registered.put(name, new DefFactory(def));
        return this;
    }

    public void load(T object, VariableSystem system) throws NoSuchFieldException, IllegalAccessException {
        Class classOf = object.getClass();

        for (Map.Entry<String, Factory> fieldID: registered.entrySet()) {
            Field field = classOf.getDeclaredField(fieldID.getKey());
            boolean access = field.isAccessible();
            field.setAccessible(true);

            Object value;
            if (system.declared(fieldID.getKey()))
                value = system.getValue(fieldID.getKey());
            else
                value = fieldID.getValue().get();

            field.set(object, value);

            field.setAccessible(access);
        }
    }

    public void save(T object, VariableSystem system) throws NoSuchFieldException, IllegalAccessException {
        Class classOf = object.getClass();

        for (Map.Entry<String, Factory> fieldID: registered.entrySet()) {
            Field field = classOf.getDeclaredField(fieldID.getKey());
            boolean access = field.isAccessible();
            field.setAccessible(true);

            system.set(fieldID.getKey(), field.get(object));

            field.setAccessible(access);
        }
    }


    public VariableSystem save(T object) throws NoSuchFieldException, IllegalAccessException {
        VariableSystem system = new VariableSystem();
        save(object, system);
        return system;
    }



    @FunctionalInterface
    public interface Factory<T> extends Serializable {
        public T get();
    }

    static class DefFactory<T> implements Factory<T>{
        final T object;

        public DefFactory(T object) {
            this.object = object;
        }

        @Override
        public T get() {
            return object;
        }
    }
}
