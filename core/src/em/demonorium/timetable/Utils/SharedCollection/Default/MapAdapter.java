package em.demonorium.timetable.Utils.SharedCollection.Default;

import java.io.Serializable;
import java.util.Map;

import em.demonorium.timetable.Utils.SharedCollection.SimpleCollection;
import em.demonorium.timetable.Utils.SharedCollection.SimpleIterator;

public class MapAdapter<KEY, VALUE> implements SimpleCollection<KEY, VALUE>, Serializable {
    private static final long serialVersionUID = 1L;
    protected final Map<KEY, VALUE> map;

    public MapAdapter(Map<KEY, VALUE> map) {
        this.map = map;
    }

    @Override
    public void add(KEY key, VALUE data) {
        map.put(key, data);
    }

    @Override
    public void set(KEY key, VALUE data) {
        map.put(key, data);
    }

    @Override
    public VALUE get(KEY key) {
        return map.get(key);
    }

    @Override
    public void remove(KEY key) {
        map.remove(key);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(KEY key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(VALUE value) {
        return map.containsValue(value);
    }

    @Override
    public KEY keyOf(VALUE value) {
        for (Map.Entry<KEY, VALUE> in: map.entrySet())
            if (in.getValue() == value)
                return in.getKey();

            return null;
    }

    @Override
    public void swap(KEY key1, KEY key2) {
        VALUE k1 = get(key1);
        set(key1, get(key2));
        set(key2, k1);
    }


    @Override
    public SimpleIterator<KEY, VALUE> iterator() {
        return new MapIterator<>(map);
    }

    public static class MapIterator<K, V> implements SimpleIterator<K, V> {
        java.util.Iterator<K> list;
        Map<K, V> collection;
        V value;
        K id;

        public MapIterator(java.util.Map<K, V> list) {
            this.list = list.keySet().iterator();
            collection = list;
            value = null;
            id = null;
        }

        @Override
        public V get() {
            return value;
        }

        @Override
        public K key() {
            return id;
        }

        @Override
        public boolean hasNext() {
            return list.hasNext();
        }

        @Override
        public V next() {
            id = list.next();
            value = collection.get(id);
            return get();
        }
    }
}
