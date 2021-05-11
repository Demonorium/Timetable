package em.demonorium.timetable.Utils.Input;

import com.badlogic.gdx.scenes.scene2d.Actor;

public interface KeyboardInputAdapter {

    public Actor getActor();
    public String getText();
    public void setText(String text);
}
