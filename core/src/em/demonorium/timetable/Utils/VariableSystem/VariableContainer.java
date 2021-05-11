package em.demonorium.timetable.Utils.VariableSystem;

import java.io.Serializable;

public interface VariableContainer {

    <T> void set(String name, T value);
    <T> void set(Variable<T> variable);
    <T> void add(Variable<T> variable);

    boolean declared(String name);
    void remove(String name);

    <T> Variable<T> get(String name);
    <T> T getValue(String name);

    <T> void defaultValue(String name, T value);
}
