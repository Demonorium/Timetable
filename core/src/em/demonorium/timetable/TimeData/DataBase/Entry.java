package em.demonorium.timetable.TimeData.DataBase;

import java.util.HashMap;

public class Entry extends IEntry {
    private static final long serialVersionUID = 1L;
    public final EntryPrototype prototype;

    HashMap<String, Object> content = new HashMap<>();

    public final DataBase BASE;
    public final int ID;

    private transient boolean destroyed = false;

    Entry(DataBase base, EntryPrototype prototype) {
        this.ID = base.ids.get();
        this.BASE = base;
        base.entryMap.put(this.ID, this);
        this.prototype = prototype;
    }

    Entry(DataBase base) {
        ID = base.ids.get();
        this.BASE = base;
        base.entryMap.put(this.ID, this);
        prototype = null;
    }

    void destroy() {
        if (destroyed) return;
        destroyed = true;

        if (prototype != null)
            prototype.actDeconstruction(this, BASE);

        BASE.entryMap.remove(ID);
        BASE.ids.free(ID);
    }

    @Override
    public int hashCode() {
        return ID;
    }

    @Override
    public<T> T get(String id) {
        return (T)content.get(id);
    }

    @Override
    public boolean contains(String id) {
        return content.containsKey(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entry entry = (Entry) o;
        return ID == entry.ID;
    }

    @Override
    public <T> void set(String id, T content) {
        this.content.put(id, content);
        if (prototype != null)
            prototype.set(this, id);
    }

    @Override
    public Entry newEntry() {
        return new Entry(BASE);
    }

    @Override
    public Entry newEntry(EntryPrototype prototype) {
        return prototype.makeEntry(BASE);
    }

    @Override
    public Entry newEntry(EntryPrototype prototype, Object... objects) {
        return prototype.makeEntry(BASE, objects);
    }
}
