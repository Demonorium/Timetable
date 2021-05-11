package em.demonorium.timetable.Factory;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

import java.util.HashMap;

public class LabelFactory {
    static private HashMap<String, Label.LabelStyle> skin = new HashMap<>();

    public static void register(String name, Label.LabelStyle style) {
        skin.put(name, style);
    }


    public static Label make(String name, String text) {
        Label l = new Label(text, skin.get(name));
        l.setAlignment(Align.center);
        return l;
    }
}
