package em.demonorium.timetable.Menu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;


import java.util.logging.Level;

import em.demonorium.timetable.BasicScreen;
import em.demonorium.timetable.Core;
import em.demonorium.timetable.Factory.LabelFactory;
import em.demonorium.timetable.Factory.TextButtonFactory;
import em.demonorium.timetable.Utils.AlignedGroup.AlignedGroup;
import em.demonorium.timetable.Utils.AlignedGroup.GroupRow;

public class IfScreen extends BasicScreen {
    public IfScreen(Core _core) {
        super(_core);
    }
    private Label WHAT;


    @Override
    public void create() {
        UI = new AlignedGroup(
                new GroupRow(30f,   1f),

                new GroupRow(2f,    10f, 80f, 10f),
                new GroupRow(10f,   10f, 5f, 30f, 10f, 30f, 5f, 10f),
                new GroupRow(8f,    10f, 80f, 10f),
                new GroupRow(10f,   10f, 80f, 10f),

                new GroupRow(30f,   1f)
        );

        UI.setActor(new Image(core.colors.getColor("base")), 1, 1, 4, 1);


        WHAT = LabelFactory.make("BIG", "");
        UI.setActor(WHAT, 4, 1);

        TextButton y = TextButtonFactory.make("BIG", "Да");
        y.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                vars.doAction("yes");
                core.back();
            }
        });
        UI.setActor(y, 2, 2);


        TextButton n = TextButtonFactory.make("BIG", "Нет");
        n.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                vars.doAction("no");
                core.back();
            }
        });
        UI.setActor(n, 2, 4);
    }

    @Override
    public void show() {
        super.show();

        Object o = vars.getValue("what");
        if (o != null) {
            if (o instanceof String)
                WHAT.setText((String)o);
            else
                Core.LOGGER.log(Level.WARNING, "Question is not string: what");
        } else
            Core.LOGGER.log(Level.WARNING, "No question for Y/N: what");

    }
}
