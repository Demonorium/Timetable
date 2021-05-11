package em.demonorium.timetable.Utils.SharedCollection;

import java.util.Iterator;

public interface SimpleIterator<K, V> extends Iterator<V> {

    public V get();
    public K key();
}
