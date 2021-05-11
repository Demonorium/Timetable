package em.demonorium.timetable.Utils.Input.Default;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class DesktopKeyboardInput extends AbstractInput {
    final TextField field;

    public DesktopKeyboardInput(TextField field) {
        this.field = field;
    }


    @Override
    public Actor getActor() {
        return field;
    }

    @Override
    public String getText() {
        return field.getText();
    }

    @Override
    public void setText(String text) {
        field.setText(text);
    }
}
