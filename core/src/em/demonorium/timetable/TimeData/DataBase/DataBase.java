package em.demonorium.timetable.TimeData.DataBase;

import java.util.HashMap;

import em.demonorium.timetable.Utils.Pools.IDPool;


public class DataBase extends IEntry {
    private static final long serialVersionUID = 1L;
    public final Entry root;
    IDPool ids;
    HashMap<Integer, Entry> entryMap;


    public DataBase() {
        ids = new IDPool();
        entryMap = new HashMap<>();
        root = new Entry(this);

    }

    public DataBase(EntryPrototype prototype) {
        ids = new IDPool();
        entryMap = new HashMap<>();
        root = prototype.makeEntry(this);
    }

    @Override
    public<T> T get(String name) {
        return root.get(name);
    }


    public Entry get(int id) {
        return entryMap.get(id);
    }

    @Override
    public boolean contains(String id) {
        return root.contains(id);
    }

    @Override
    public <T>void set(String name, T content) {
        root.set(name, content);
    }

    @Override
    public Entry newEntry() {
        return root.newEntry();
    }

    @Override
    public Entry newEntry(EntryPrototype prototype) {
        return root.newEntry(prototype);
    }

    @Override
    public Entry newEntry(EntryPrototype prototype, Object... objects) {
        return root.newEntry(prototype, objects);
    }

    public void destroy(int id) {
        get(id).destroy();
    }
}
