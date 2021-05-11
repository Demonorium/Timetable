package em.demonorium.timetable.Utils.Input.Default;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import java.util.TreeMap;

import em.demonorium.timetable.Utils.Input.KeyboardInputAdapter;
import em.demonorium.timetable.Utils.Input.KeyboardInputAdapterFactory;
import em.demonorium.timetable.Utils.Input.UniversalFieldStyle;

public class DesktopKeyboardInputFactory implements KeyboardInputAdapterFactory {
    private TreeMap<String, TextField.TextFieldStyle> map = new TreeMap<>();

    @Override
    public void register(String id, UniversalFieldStyle style) {
        TextField.TextFieldStyle newStyle = new TextField.TextFieldStyle();
        newStyle.font = style.font;
        newStyle.fontColor = style.fontColor;
        newStyle.disabledFontColor = style.offFontColor;

        newStyle.background         = style.base;
        newStyle.disabledBackground = style.disabled;
        newStyle.focusedBackground  = style.selected;

        newStyle.selection = style.selection;
        newStyle.cursor = style.cursor;
        map.put(id, newStyle);
    }

    @Override
    public KeyboardInputAdapter get(String id, String defaultValue) {
        return new DesktopKeyboardInput(new TextField(defaultValue, map.get(id)));
    }
}
