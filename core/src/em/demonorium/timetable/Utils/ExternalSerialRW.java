package em.demonorium.timetable.Utils;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import em.demonorium.timetable.Utils.VariableSystem.Variable;
import em.demonorium.timetable.Utils.VariableSystem.VariableSystem;

public abstract class ExternalSerialRW<T> implements Externalizable {
    protected static final ObjectRW RW = new ObjectRW<>();
    protected static final long serialVersionUID = 1L;

    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        try {
            objectOutput.writeObject(RW.save(this));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        try {
            RW.load(this, (VariableSystem) objectInput.readObject());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }



}
