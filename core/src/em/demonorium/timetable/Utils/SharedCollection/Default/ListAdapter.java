package em.demonorium.timetable.Utils.SharedCollection.Default;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import em.demonorium.timetable.Utils.SharedCollection.SimpleCollection;
import em.demonorium.timetable.Utils.SharedCollection.SimpleIterator;

public class ListAdapter<VALUE> implements SimpleCollection<Integer, VALUE>, Serializable {
    private static final long serialVersionUID = 1L;
    protected final List<VALUE> list;

    public ListAdapter(List<VALUE> list) {
        this.list = list;
    }

    @Override
    public void add(Integer integer, VALUE data) {
        if (integer >= size())
            list.add(data);
        else
            list.add(integer, data);
    }

    @Override
    public void set(Integer integer, VALUE data) {
        list.set(integer, data);

    }

    @Override
    public Integer keyOf(VALUE value) {
        return list.indexOf(value);
    }

    @Override
    public VALUE get(Integer integer) {
        return list.get(integer);
    }

    @Override
    public void remove(Integer integer) {
        list.remove((int) integer);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean containsKey(Integer integer) {
        return (integer >= 0) && (integer < size());
    }

    @Override
    public boolean containsValue(VALUE value) {
        return list.contains(value);
    }

    @Override
    public void swap(Integer key1, Integer key2) {
        VALUE k1 = get(key1);
        set(key1, get(key2));
        set(key2, k1);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public SimpleIterator<Integer, VALUE> iterator() {
        return new ListIterator<VALUE>(list.listIterator());
    }

    public static class ListIterator<V> implements SimpleIterator<Integer, V> {
        java.util.ListIterator<V> list;
        V value;
        int id;

        public ListIterator(java.util.ListIterator<V> list) {
            this.list = list;
            value = null;
            id = -1;
        }

        @Override
        public V get() {
            return value;
        }

        @Override
        public Integer key() {
            return id;
        }

        @Override
        public boolean hasNext() {
            return list.hasNext();
        }

        @Override
        public V next() {
            value = list.next();
            ++id;
            return get();
        }
    }
}
