package em.demonorium.timetable.Utils.VariableSystem;

public interface ActionContainer {
    void setAction(String name, Action action);
    void doAction(String name);
}
