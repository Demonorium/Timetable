package em.demonorium.timetable.Utils.Pools;

import java.io.Serializable;
import java.util.LinkedList;

public class UniversalPool<VALUE> extends PoolBase<VALUE> implements Serializable {
    protected LinkedList<VALUE> values = new LinkedList<>();

    @Override
    public VALUE get() {
        return values.pop();
    }

    @Override
    public void free(VALUE object) {
        values.add(object);
    }
}
