package em.demonorium.timetable.Utils.SharedCollection;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;

import em.demonorium.timetable.Utils.SharedCollection.Default.ListAdapter;
import em.demonorium.timetable.Utils.SharedCollection.Default.MapAdapter;
import em.demonorium.timetable.Utils.SharedCollection.Default.SharedList;

public class SharedCollection<KEY, VALUE> implements SimpleCollection<KEY, VALUE>, Editable<KEY, VALUE>, Externalizable {
    private static final long serialVersionUID = 1L;

    protected SimpleCollection<KEY, VALUE> content;
    protected transient ArrayList<Editable<KEY, VALUE>> linked = new ArrayList<>();

    //For external
    public SharedCollection() {
        linked = new ArrayList<>();
    }

    public<T> SharedCollection(T object) {
        if (object instanceof Map)
            content = new MapAdapter<>((Map<KEY, VALUE>)object);
        else if (object instanceof List) {
            content = new ListAdapter((List) object);
        }
        else
            throw new IllegalArgumentException();
    }

    public SharedCollection(SimpleCollection<KEY, VALUE> collection) {
        this.content = collection;
    }


    public void setContent(SimpleCollection<KEY, VALUE> content) {
        if (this.content == null) {
            this.content = content;
            SimpleIterator<KEY, VALUE> iter = content.iterator();
            while (iter.hasNext()) {
                update(iter.key());
            }
        } else {
            clear();
            SimpleIterator<KEY, VALUE> iter = content.iterator();
            while (iter.hasNext()) {
                add(iter.key(), iter.get());
            }
        }

    }

    public void updateByValue(VALUE value) {
        update(keyOf(value));
    }

    @Override
    public VALUE get(KEY key) {
        return content.get(key);
    }
    @Override
    public void add(KEY key, VALUE value) {
        content.add(key, value);


        for (Editable<KEY, VALUE> rem: linked)
            rem.added(key, value);

    }


    @Override
    public void set(KEY key, VALUE value) {
        content.set(key, value);
        update(key);
    }

    @Override
    public void remove(KEY key) {
        content.remove(key);
        for (Editable<KEY, VALUE> rem: linked)
            rem.removed(key);
    }
    @Override
    public void clear() {
        content.clear();
        for (Editable<KEY, VALUE> rem: linked)
            rem.cleared();
    }
    @Override
    public int size() {
        return content.size();
    }

    @Override
    public void swap(KEY key1, KEY key2) {
        content.swap(key1, key2);
        for (Editable<KEY, VALUE> rem: linked) {
            rem.updated(key1);
            rem.updated(key2);
        }
    }


    public void register(Editable<KEY, VALUE> editable) {
        if (editable == this) return;

        if (!linked.contains(editable))
            linked.add(editable);
    }

    @Override
    public KEY keyOf(VALUE value) {
        return content.keyOf(value);
    }

    public void update(KEY key) {
        for (Editable<KEY, VALUE> rem: linked) {
            rem.updated(key);
        }
    }

    @Override
    public void added(KEY key, VALUE data) {
        add(key, data);
    }

    @Override
    public void removed(KEY key) {
        remove(key);
    }

    @Override
    public void cleared() {
        clear();
    }

    @Override
    public void updated(KEY key) {
        update(key);
    }

    public void removeRegistration(Editable<KEY, VALUE> value) {
        linked.remove(value);
    }

    @Override
    public SimpleIterator<KEY, VALUE> iterator() {
        return content.iterator();
    }

    @Override
    public boolean isEmpty() {
        return content.isEmpty();
    }

    @Override
    public boolean containsKey(KEY key) {
        return content.containsKey(key);
    }

    @Override
    public boolean containsValue(VALUE value) {
        return content.containsValue(value);
    }

    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeObject(content);
    }

    @Override
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        content = (SimpleCollection<KEY, VALUE>) objectInput.readObject();
    }
}
