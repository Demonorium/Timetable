package em.demonorium.timetable.Utils.Input.Default;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.TreeMap;

import em.demonorium.timetable.Factory.TextButtonFactory;
import em.demonorium.timetable.Utils.Input.KeyboardInputAdapter;
import em.demonorium.timetable.Utils.Input.KeyboardInputAdapterFactory;
import em.demonorium.timetable.Utils.Input.UniversalFieldStyle;

public class AndroidKeyboardInputFactory implements KeyboardInputAdapterFactory {
    private TreeMap<String, TextButton.TextButtonStyle> map = new TreeMap<>();

    @Override
    public void register(String id, UniversalFieldStyle style) {
        TextButton.TextButtonStyle newStyle = new TextButton.TextButtonStyle();
        newStyle.font       = style.font;
        newStyle.fontColor  = style.fontColor;
        newStyle.disabledFontColor = style.offFontColor;

        newStyle.up         = style.base;
        newStyle.down       = style.selected;
        newStyle.disabled   = style.disabled;
        newStyle.over       = style.over;



        map.put(id, newStyle);
    }

    @Override
    public KeyboardInputAdapter get(String id, String defaultValue) {
        TextButton button = new TextButton(defaultValue, map.get(id));
        TextButtonFactory.castrator.cst(button);
        return new AndroidKeyboardInput(button);
    }
}
