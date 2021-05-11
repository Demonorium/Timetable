package em.demonorium.timetable.Utils.Pools;

public abstract class PoolBase<T> {


    public abstract T get();
    public abstract void free(T object);


    @SafeVarargs
    public final void free(T... objects) {
        for (T o: objects)
            free(o);
    }


}
