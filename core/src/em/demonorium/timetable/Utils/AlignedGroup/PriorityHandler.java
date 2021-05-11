package em.demonorium.timetable.Utils.AlignedGroup;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

class PriorityHandler<Key, Value> {
    private final TreeMap<Integer, HashMap<Key, Value>> container;
    private final int defaultID;

    private Iterator<HashMap<Key, Value>> treeIterator;
    private Iterator<Map.Entry<Key, Value>> finalIterator;


    PriorityHandler() {
        this.container = new TreeMap<>();
        this.defaultID = 0;
    }

    PriorityHandler(int defaultID) {
        this.container = new TreeMap<>();
        this.defaultID = defaultID;
    }

    public Value addEntry(Key key, Value value, int id) {
        HashMap<Key, Value> entry = container.get(id);
        if (entry == null) {
            entry = new HashMap<>();
            container.put(id, entry);
            entry.put(key, value);
        } else {
            Value val = entry.get(key);
            if (val == null)
                entry.put(key, value);
            else {
                return val;
            }
        }

        return null;
    }

    public int getDefaultID() {
        return defaultID;
    }

    public void addEntry(Key key, Value value) {
        addEntry(key, value, defaultID);
    }

    public void begin() {
        treeIterator = container.values().iterator();
        if (treeIterator.hasNext())
            finalIterator = treeIterator.next().entrySet().iterator();
    }

    public boolean hasNext() {
        if ((finalIterator != null) && (finalIterator.hasNext()))
            return true;
        return (treeIterator != null) && (treeIterator.hasNext());
    }

    public Map.Entry<Key, Value> next() {
        if ((finalIterator == null) || (!finalIterator.hasNext()))
            finalIterator = treeIterator.next().entrySet().iterator();

        return finalIterator.next();
    }

    public void end() {
        finalIterator = null;
        treeIterator = null;
    }


}
