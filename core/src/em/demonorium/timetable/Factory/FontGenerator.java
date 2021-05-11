package em.demonorium.timetable.Factory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontGenerator {
    private static final String fontline = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZабвгдеёжзийклмнопрстуфхцчшщъыьэюяЯЮЭЬЫЪЩШЧЦХФУТСРПОНМЛКЙИЗЖЁЕДГВБА1234567890!@#$%^&*()_+-=|\\\"':;<>,.?/";



    private FreeTypeFontGenerator regular;
    private FreeTypeFontGenerator bold;
    private FreeTypeFontGenerator italic;
    private FreeTypeFontGenerator boldItalic;

    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    public FontGenerator(String fontname, String exc) {
        regular     = new FreeTypeFontGenerator(Gdx.files.internal("Font/" + fontname + "-Regular."     + exc));
        bold        = new FreeTypeFontGenerator(Gdx.files.internal("Font/" + fontname + "-Bold."        + exc));
        italic      = new FreeTypeFontGenerator(Gdx.files.internal("Font/" + fontname + "-Italic."      + exc));
        boldItalic  = new FreeTypeFontGenerator(Gdx.files.internal("Font/" + fontname + "-BoldItalic."  + exc));

        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = fontline;
    }


    public FontGroup generate(Color color, int size) {
        parameter.color = color;
        parameter.size = size;

        FontGroup group = new FontGroup();

        group.regular    = regular.generateFont(parameter);
        group.bold       = bold.generateFont(parameter);
        group.italic     = italic.generateFont(parameter);
        group.boldItalic = boldItalic.generateFont(parameter);

        return group;
    }



    public static class FontGroup {
        public BitmapFont regular;
        public BitmapFont bold;
        public BitmapFont italic;
        public BitmapFont boldItalic;
    }
}
