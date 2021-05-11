package em.demonorium.timetable.Utils.SharedCollection;

public interface Editable<KEY, VALUE> {
    void added(KEY key, VALUE data);
    void removed(KEY key);
    void cleared();
    void updated(KEY key);
}
