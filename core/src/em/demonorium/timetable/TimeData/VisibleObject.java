package em.demonorium.timetable.TimeData;

import com.badlogic.gdx.scenes.scene2d.ui.Button;

import java.io.Externalizable;
import java.io.Serializable;

import em.demonorium.timetable.Core;

public interface VisibleObject extends Externalizable {
    Button getButton();
    void edit(Core core);
}
