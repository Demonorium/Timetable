package em.demonorium.timetable.Menu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import em.demonorium.timetable.BasicScreen;
import em.demonorium.timetable.Core;
import em.demonorium.timetable.Factory.TextButtonFactory;
import em.demonorium.timetable.TimeData.DataAPI;
import em.demonorium.timetable.Utils.AlignedGroup.AlignedGroup;
import em.demonorium.timetable.Utils.AlignedGroup.GroupRow;

public class EditCategoryMenu extends BasicScreen {
    public EditCategoryMenu(Core _core) {
        super(_core, _core.colors.getBasicColor("base"));
    }


    @Override
    public void create() {
        final float space = 10f;
        UI = new AlignedGroup(
                new GroupRow(space, 1f),

                new GroupRow(8f, space, 40f, space),
                new GroupRow(space / 1.5f, 1f),
                new GroupRow(8f, space, 40f, space),
                new GroupRow(space * 1.25f, 1f),


                new GroupRow(15f, space, 70f, space),
                new GroupRow(space / 1.5f, 1f),
                new GroupRow(15f, space, 70f, space),
                new GroupRow(space / 1.5f, 1f),
                new GroupRow(15f, space, 70f, space),
                new GroupRow(space / 1.5f, 1f),
                new GroupRow(15f, space, 70f, space),

                new GroupRow(space, 1f)
        );



        {
            TextButton button = TextButtonFactory.make("BIG", "Выбранный день");
            button.addListener(new TextButtonFactory.UpListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    core.setScreen("dayedit");
                }
            });
            UI.setActor(button, 11, 1);
        }



        {
            TextButton button = TextButtonFactory.make("BIG", "Задачи");
            button.addListener(new TextButtonFactory.UpListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    core.varsOf("taskList").setFlag("loadDate");
                    core.varsOf("taskList").set("date", DataAPI.getSelectedDate());
                    core.setScreen("taskList");
                }
            });
            UI.setActor(button, 9, 1);
        }

        {
            TextButton button = TextButtonFactory.make("BIG", "Предметы");
            button.addListener(new TextButtonFactory.UpListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    core.varsOf("subSelect").setFlag("isSelect",false);
                    core.setScreen("subSelect");
                }
            });
            UI.setActor(button, 7, 1);
        }

        {
            TextButton button = TextButtonFactory.make("BIG", "Расписание");
            button.addListener(new TextButtonFactory.UpListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    core.setScreen("weekSelect");
                }
            });
            UI.setActor(button, 5, 1);
        }
        //////////////////

        {
            TextButton button = TextButtonFactory.make("BIG", "Настройки");
            button.addListener(new TextButtonFactory.UpListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    core.setScreen("settings");
                }
            });
            UI.setActor(button, 3, 1);
        }

        {
            TextButton button = TextButtonFactory.make("BIG", "Назад");
            button.addListener(new TextButtonFactory.UpListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    core.back();
                }
            });
            UI.setActor(button, 1, 1);
        }
    }
}
