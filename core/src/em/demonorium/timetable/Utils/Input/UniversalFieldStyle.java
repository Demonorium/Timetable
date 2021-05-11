package em.demonorium.timetable.Utils.Input;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class UniversalFieldStyle {
    public BitmapFont font;
    public Color fontColor, offFontColor;

    public Drawable
            base,
            selected,
            over,
            disabled,
            selection,
            cursor;
}
