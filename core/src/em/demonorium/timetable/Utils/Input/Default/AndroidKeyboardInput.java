package em.demonorium.timetable.Utils.Input.Default;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import em.demonorium.timetable.Factory.TextButtonFactory;
import em.demonorium.timetable.Utils.Input.KeyboardInputAdapter;

public class AndroidKeyboardInput extends AbstractInput {
    private final TextButton button;


    public AndroidKeyboardInput(TextButton button) {
        this.button = button;

        button.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.input.getTextInput(input, "", getText(), "");
            }
        });
    }


    @Override
    public Actor getActor() {
        return button;
    }

    @Override
    public String getText() {
        return button.getLabel().getText().toString();
    }

    @Override
    public void setText(String text) {
        button.setText(text);
    }

}
