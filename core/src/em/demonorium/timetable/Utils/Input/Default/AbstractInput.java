package em.demonorium.timetable.Utils.Input.Default;

import com.badlogic.gdx.Input;

import em.demonorium.timetable.Utils.Input.KeyboardInputAdapter;

public abstract class AbstractInput implements KeyboardInputAdapter {
    protected final TextInputListener input;

    AbstractInput() {
        input = new TextInputListener();
    }


    class TextInputListener implements Input.TextInputListener {
        @Override
        public void input(String text) {
            setText(text);
        }

        @Override
        public void canceled() {
        }
    }
}
