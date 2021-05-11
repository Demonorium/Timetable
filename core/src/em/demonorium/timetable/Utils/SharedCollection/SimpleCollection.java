package em.demonorium.timetable.Utils.SharedCollection;

import java.io.Serializable;

public interface SimpleCollection<KEY, VALUE> extends Serializable, Iterable<VALUE>, Cloneable {
    void add(KEY key, VALUE data);
    void set(KEY key, VALUE data);
    VALUE get(KEY key);
    void remove(KEY key);
    int size();

    void swap(KEY key1, KEY key2);
    void clear();

    KEY keyOf(VALUE value);


    @Override
    SimpleIterator<KEY, VALUE> iterator();


    boolean isEmpty();
    boolean containsKey(KEY key);
    boolean containsValue(VALUE value);
}
