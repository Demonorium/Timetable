package em.demonorium.timetable.TimeData.DataBase;

import java.io.Serializable;

import jdk.nashorn.internal.runtime.regexp.joni.constants.EncloseType;

public abstract class IEntry implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    public abstract <T> T get(String id);
    public abstract boolean contains(String id);
    public abstract <T> void set(String id, T content);

    public abstract Entry newEntry();
    public abstract Entry newEntry(EntryPrototype prototype);
    public abstract Entry newEntry(EntryPrototype prototype, Object ... objects);

    private IEntry skip(String ... strings) {
        IEntry entry = this;

        for (int i = 0; i < (strings.length-1); ++i) {
            entry = entry.get(strings[i]);
        }

        return entry;
    }

    public <T> T get(String ... strings) {
        return skip(strings).get(strings[strings.length - 1]);
    }

}
