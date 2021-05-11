package em.demonorium.timetable.Utils.SharedCollection.Default;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import em.demonorium.timetable.TimeData.SimpleDate;
import em.demonorium.timetable.Utils.SharedCollection.SharedCollection;
import em.demonorium.timetable.Utils.SharedCollection.SimpleCollection;
import em.demonorium.timetable.Utils.SharedCollection.SimpleIterator;

public class SharedList<T> extends SharedCollection<Integer, T> {
    private static final long serialVersionUID = 1L;

    public SharedList() {
    }

    public <T1> SharedList(T1 object) {
        super(object);
    }

    public SharedList(SimpleCollection<Integer, T> collection) {
        super(collection);
    }

    public void add(T object) {
        add(size(), object);
    }

    public void sort(Comparator<T> comparator) {
        TreeSet<T> set = new TreeSet<>(comparator);
        for (T obj: content)
            set.add(obj);

        clear();
        
        for (T obj: set)
            add(obj);
    }
}
