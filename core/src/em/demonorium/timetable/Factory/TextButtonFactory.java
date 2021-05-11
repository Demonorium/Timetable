package em.demonorium.timetable.Factory;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.lang.reflect.Field;
import java.util.HashMap;

import em.demonorium.timetable.Utils.AlignedGroup.AlignedGroup;
import em.demonorium.timetable.Utils.AlignedButton;
import em.demonorium.timetable.Utils.FixedButton;

public class TextButtonFactory {
    static private HashMap<String, TextButton.TextButtonStyle> skin = new HashMap<>();

    public static void register(String name, TextButton.TextButtonStyle style) {
        skin.put(name, style);
    }


    public static TextButton make(String name, String text) {
        TextButton button = new TextButton(text, skin.get(name));
        castrator.cst(button);
        return button;
    }

    public static <T extends Actor> FixedButton<T> make(String name, T actor, Float w, Float h) {
        FixedButton<T> button = new FixedButton<>(skin.get(name), actor, w, h);
        castrator.cst(button);
        return button;
    }

    public static <T extends Actor> FixedButton<T> make(String name, T actor, Float w, Float h, boolean castration) {
        FixedButton<T> button = new FixedButton<>(skin.get(name), actor, w, h);
        if (castration)
            castrator.cst(button);
        return button;
    }

    public static TextButton make(String name, String text, boolean castration) {
        TextButton button = new TextButton(text, skin.get(name));
        if (castration)
            castrator.cst(button);
        return button;
    }

    public static Button make(String name) {
        Button button = new Button(skin.get(name));
        castrator.cst(button);
        return button;
    }

    public static Button make(String name, boolean castration) {
        Button button = new Button(skin.get(name));
        if (castration)
            castrator.cst(button);
        return button;
    }

    public static AlignedButton make(String name, Float h, AlignedGroup group) {
        return new AlignedButton(skin.get(name), h, group);
    }

    public static AlignedButton make(String name, Float h, AlignedGroup group, boolean castration) {
        AlignedButton button = new AlignedButton(skin.get(name), h, group);
        if (castration)
            castrator.cst(button);
        return button;
    }


    public static abstract class UpListener<T> extends InputListener {
        protected final T object;

        public UpListener(T object) {
            this.object = object;
        }

        public UpListener() {
            object = null;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            return true;
        }


    }




    public static class ButtonCastrator {


        public void cst(Button button, Class casted) {
            button.removeListener(button.getClickListener());
            ClickListener listener = new ClickListener();

            try {
                Field field = casted.getDeclaredField("clickListener");

                field.setAccessible(true);

                field.set(button, listener);
                button.addListener(listener);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        public void cst(TextButton button) {
            Class aClass = button.getClass();
            Class<Button> casted = aClass.getSuperclass();
            cst(button, casted);
        }

        public void cst(FixedButton button) {
            Class aClass = button.getClass();
            Class<Button> casted = aClass.getSuperclass();
            cst(button, casted);
        }

        public void cst(Button button) {
            cst(button, button.getClass());
        }

        public void cst(AlignedButton button) {
            Class aClass = button.getClass();
            Class casted = aClass.getSuperclass();
            casted = casted.getSuperclass();
            cst(button, casted);
        }
    } 
    public static ButtonCastrator castrator = new ButtonCastrator();
}
