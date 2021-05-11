package em.demonorium.timetable.Utils.VariableSystem;

import com.badlogic.gdx.scenes.scene2d.Actor;

import java.io.Serializable;
import java.util.HashMap;

public class VariableSystem implements IVariableSystem, Serializable {
    final HashMap<String, Variable> variables = new HashMap<>();



    public boolean declared(String name) {
        return variables.containsKey(name);
    }

    @Override
    public void remove(String name) {
        variables.remove(name);
    }


    public <T> void set(String name, T value) {
        if (declared(name)) {
            variables.get(name).setValue(value);
        } else {
            add(new Variable<T>(name, value));
        }
    }

    @Override
    public <T> void set(Variable<T> variable) {
        if (declared(variable.getName()))
            variables.get(variable.getName()).setValue(variable.getValue());

        add(variable);
    }

    @Override
    public <T> void add(Variable<T> variable) {
        variables.put(variable.getName(), variable);
    }


    public <T> Variable<T> get(String id) {
        return (Variable<T>)variables.get(id);
    }

    public <T> T getValue(String id) {
        return (T)get(id).getValue();
    }


    @Override
    public void setAction(String name, Action action) {
        set(name, action);
    }

    @Override
    public void doAction(String name) {
        if (declared(name))
            ((Action)getValue(name)).action();
    }

    @Override
    public boolean testAndSet(String name, boolean v)  {
        boolean value = testFlag(name);
        setFlag(name, v);
        return value;
    }

    @Override
    public boolean testFlag(String string) {
        if (declared(string))
            return (boolean) getValue(string);
        return false;
    }

    @Override
    public <T> void defaultValue(String name, T value) {
        if (!declared(name))
            add(new Variable<Object>(name, value));
    }

    @Override
    public void setFlag(String name, boolean value) {
        set(name, value);
    }

    @Override
    public void setFlag(String name) {
        set(name, true);
    }
}
