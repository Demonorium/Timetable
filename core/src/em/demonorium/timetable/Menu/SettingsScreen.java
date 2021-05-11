package em.demonorium.timetable.Menu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import em.demonorium.timetable.BasicScreen;
import em.demonorium.timetable.Core;
import em.demonorium.timetable.Factory.TextButtonFactory;

public class SettingsScreen extends BasicScreen {
    public SettingsScreen(Core _core) {
        super(_core, _core.colors.getBasicColor("base"));
    }

    @Override
    public void create() {

        TextButton save, cancel;

        cancel = TextButtonFactory.make("MEDIUM", "Отмена");
        cancel.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                core.back();
            }
        });

        save = TextButtonFactory.make("MEDIUM", "Сохранить");
        save.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                core.SAVE_SETTINGS();
                core.back();
            }
        });

        TextButton colors = TextButtonFactory.make("BIG", "Цвета");
        colors.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                core.setScreen("colorSelect");
            }
        });

        beginUI()
                .r(1f)
                .r(8f).c(0.1f).c(0.35f, cancel).c(0.1f).c(0.35f, save).c(0.1f)
                .r(1f)

                .r(10f)

                .r(10f).c(0.1f).c(0.8f, colors).c(0.1f)
                .r(10f)

                .r(10f).c(0.1f).c(0.8f).c(0.1f)
                .r(10f)

                .r(10f).c(0.1f).c(0.8f).c(0.1f)
                .r(10f)

                .r(10f).c(0.1f).c(0.8f).c(0.1f)
                ;
        endUI();
    }
}
