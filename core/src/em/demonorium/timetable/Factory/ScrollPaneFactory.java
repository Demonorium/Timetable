package em.demonorium.timetable.Factory;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import java.util.HashMap;

public class ScrollPaneFactory {
    static private HashMap<String, ScrollPane.ScrollPaneStyle> skin = new HashMap<>();

    public static void register(String name, ScrollPane.ScrollPaneStyle style) {
        skin.put(name, style);
    }


    public static ScrollPane make(String name, Actor actor) {
        return new ScrollPane(actor, skin.get(name));
    }
}
