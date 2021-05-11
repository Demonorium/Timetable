package em.demonorium.timetable.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.LinkedList;

import em.demonorium.timetable.BasicScreen;
import em.demonorium.timetable.Core;
import em.demonorium.timetable.Factory.LabelFactory;
import em.demonorium.timetable.Factory.Locale;
import em.demonorium.timetable.Factory.ScrollPaneFactory;
import em.demonorium.timetable.Factory.TextButtonFactory;
import em.demonorium.timetable.Utils.AbstractLocalFactory;
import em.demonorium.timetable.Utils.ButtonList;
import em.demonorium.timetable.Utils.SharedCollection.Default.ListAdapter;
import em.demonorium.timetable.Utils.SharedCollection.Default.SharedList;

public class ColorSelectScreen extends BasicScreen {
    public ColorSelectScreen(Core _core) {
        super(_core, _core.colors.getBasicColor("base"));
    }

    private SharedList<String> files;
    private ButtonList<String> colors;
    private ColorsFactory factory;


    public static class ColorsFactory extends AbstractLocalFactory<String, ColorSelectScreen> {

        public ColorsFactory(ColorSelectScreen colorSelectScreen) {
            super(colorSelectScreen);
        }

        @Override
        protected Button newButton(String data) {
            return TextButtonFactory.make("BIG", Locale.get(data), false);
        }

        @Override
        protected void onSelected() {
            Source.backBT.setText("Выбрать");
        }

        @Override
        protected void onUnselected() {
            Source.backBT.setText("Назад");
        }
    }

    private TextButton backBT;


    @Override
    public void show() {
        super.show();
    }

    @Override
    public void create() {
        factory = new ColorsFactory(this);
        files = new SharedList<>(new ListAdapter<>(new LinkedList<String>()));
        colors = new ButtonList<>(files, factory);


        backBT = TextButtonFactory.make("BIG", "Назад");
        backBT.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (colors.hasSelected()) {
                    Core.settings.set("color", colors.getSelected());
                }
                core.back();
            }
        });


        FileHandle colorD = Gdx.files.internal("Colors");
        for (FileHandle pal : colorD.list()) {
            if (!pal.isDirectory())
                files.add(pal.nameWithoutExtension());
        }

        ScrollPane pane = ScrollPaneFactory.make("main", colors.getVL());
        Label label = LabelFactory.make("BIG_NO_B", "Выбор цвета");

        beginUI()
                .r(1f)
                .r(8f).c(0.1f).c(0.8f, backBT).c(0.1f)
                .r(1f)

                .r(78f).c(0.1f).c(0.8f, pane).c(0.1f)
                .r(2f)
                .r(10f).c(1f, label)
        ;


        endUI();

    }
}
