package em.demonorium.timetable.TimeData.DataBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import em.demonorium.timetable.Utils.VariableSystem.Action;
import sun.security.krb5.internal.PAData;

public class EntryPrototype implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Pair> defaultField = new ArrayList<>();
    private String[] constructor;
    private EntryAction preConstruction, postConstruction;
    private EntryAction deconstruction;
    private HashMap<String, EntryAction> setters = new HashMap<>();




    private static class Pair implements Serializable {
        String key;
        Factory value;

        public Pair(String field, Factory content) {
            this.key = field;
            this.value = content;
        }
    }


    public void set(Entry entry, String id) {
        EntryAction action = setters.get(id);
        if (action != null)
            action.action(entry, entry.BASE);
    }
    public void addSetter(String id, EntryAction action){
        setters.put(id, action);
    }
    public void actPreConstruction(Entry entry, DataBase base) {
        if (preConstruction != null)
            preConstruction.action(entry, base);
    }

    public void setPreConstruction(EntryAction preConstruction) {
        this.preConstruction = preConstruction;
    }

    public void actPostConstruction(Entry entry, DataBase base) {
        if (postConstruction != null)
            postConstruction.action(entry, base);
    }

    public void setPostConstruction(EntryAction postConstruction) {
        this.postConstruction = postConstruction;
    }
    public void actDeconstruction(Entry entry, DataBase base) {
        if (deconstruction != null)
            deconstruction.action(entry, base);
    }

    public void setDeconstruction(EntryAction deconstruction) {
        this.deconstruction = deconstruction;
    }
    public void register(String field, Factory content) {
        defaultField.add(new Pair(field, content));
    }

    public <T> void register(String field, T content) {
        defaultField.add(new Pair(field, new DefFactory<T>(content)));
    }

    public void setConstructor(String ... constructor) {
        this.constructor = constructor;
    }

    public Entry makeEntry(DataBase base) {
        Entry entry = new Entry(base, this);

        actPreConstruction(entry, base);
        for (Pair pair: defaultField) {
            entry.content.put(pair.key, pair.value.get());
        }
        actPostConstruction(entry, base);
        return entry;
    }

    public Entry makeEntry(DataBase base, Object ... objects) {
        Entry entry = new Entry(base, this);

        actPreConstruction(entry, base);

        for (Pair pair: defaultField) {
            entry.content.put(pair.key, pair.value.get());
        }
        if (constructor != null)
            for (int i = 0; i < Math.min(constructor.length, objects.length); ++i) {
                entry.content.put(constructor[i], objects[i]);
            }

        actPostConstruction(entry, base);
        return entry;
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
    @FunctionalInterface
    public interface EntryAction extends Serializable {
        void action(Entry entry, DataBase base);
    }
}
