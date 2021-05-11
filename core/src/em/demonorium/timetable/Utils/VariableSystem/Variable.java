package em.demonorium.timetable.Utils.VariableSystem;

import java.io.Serializable;

public class Variable<T> implements Serializable {
    private final String name;
    private T value;

    public Variable(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public String getType() {
        return value.getClass().getName();
    }

    public void setValue(T value) {
        this.value = value;
    }

    public static boolean SameType(Variable v1, Variable v2) {
        return v1.getType() == v2.getType();
    }
}
