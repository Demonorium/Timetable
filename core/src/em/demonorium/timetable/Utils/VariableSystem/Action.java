package em.demonorium.timetable.Utils.VariableSystem;

import java.io.Serializable;


public interface Action extends Serializable {
    void action();

    static class _NONE_ACTION implements Action {
        public void action() {
        }
    }
    public static final Action NONE_ACTION = new _NONE_ACTION();
}
