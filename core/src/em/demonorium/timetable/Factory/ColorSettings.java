package em.demonorium.timetable.Factory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import java.util.HashMap;

public class ColorSettings {
    private Texture texture;
    private TextureAtlas atlas;
    private HashMap<String, Color> basicColors;
    private HashMap<String, Drawable> colors;

    public ColorSettings(String name, String ... names) {
        texture = new Texture(name);
        basicColors = new HashMap<>();
        colors = new HashMap<>();

        atlas = new TextureAtlas();
        int pix = 0;

        TextureData data = texture.getTextureData();
        data.prepare();
        Pixmap map = data.consumePixmap();
        for (String id: names) {
            atlas.addRegion(id, new TextureRegion(texture, pix, 0, 1, 1));
            basicColors.put(id, new Color(map.getPixel(pix, 0)));
            colors.put(id, new SpriteDrawable(new Sprite(atlas.findRegion(id))));
            pix += 1;
        }
        data.disposePixmap();
    }

    public Color getBasicColor(String id) {
        return basicColors.get(id);
    }

    public Drawable getColor(String id) {
        return colors.get(id);
    }
}
