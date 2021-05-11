package em.demonorium.timetable.TimeData.Models.VisibleObjects;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import em.demonorium.timetable.Core;
import em.demonorium.timetable.Factory.TextButtonFactory;
import em.demonorium.timetable.TimeData.VisibleObject;

public class VisibleText implements VisibleObject{
    private static final long serialVersionUID = 1L;
    private String txt;
    private transient TextButton text;

    public VisibleText() {
        this.text = TextButtonFactory.make("MEDIUM", "text", false);
    }
    public VisibleText(String text) {
        txt = text;
        this.text = TextButtonFactory.make("MEDIUM", text, false);
    }

    @Override
    public Button getButton() {
        return text;
    }

    @Override
    public void edit(Core core) {

        core.setScreen("editTask");
    }

    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeObject(txt);
    }

    @Override
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        txt = (String)objectInput.readObject();
        text.setText(txt);
    }
}
