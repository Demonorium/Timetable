package em.demonorium.timetable.Menu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import em.demonorium.timetable.BasicScreen;
import em.demonorium.timetable.Core;
import em.demonorium.timetable.Factory.LabelFactory;
import em.demonorium.timetable.Factory.ScrollPaneFactory;
import em.demonorium.timetable.Factory.TextButtonFactory;
import em.demonorium.timetable.TimeData.DataAPI;
import em.demonorium.timetable.TimeData.SimpleDate;
import em.demonorium.timetable.Utils.AbstractLocalFactory;
import em.demonorium.timetable.Utils.AlignedGroup.AlignedGroup;
import em.demonorium.timetable.Utils.AlignedGroup.GroupRow;
import em.demonorium.timetable.Utils.ButtonList;
import em.demonorium.timetable.Utils.SharedCollection.Default.SharedList;
import em.demonorium.timetable.Utils.VariableSystem.VariableSystem;

public class TimetableSelectScreen extends BasicScreen {
    AbstractLocalFactory<SimpleDate, TimetableSelectScreen> buttonFactory;

    ButtonList<SimpleDate> dates;
    SharedList<SimpleDate> timetable;
    GregorianCalendar standart;

    int editDate =  -1;

    public TimetableSelectScreen(Core _core) {
        super(_core, _core.colors.getBasicColor("base"));
    }


    @Override
    public void create() {
        float space = 9f;
        UI = new AlignedGroup(
                new GroupRow(1f, 1f),
                new GroupRow(8f, 0.1f, 0.35f, 0.1f, 0.35f, 0.1f),
                new GroupRow(1f, 1f),

                new GroupRow(1f, 1f),
                new GroupRow(8f, 0.1f, 0.6f/3f, 0.1f, 0.6f/3f, 0.1f, 0.6f/3f, 0.1f),
                new GroupRow(1f, 1f),

                new GroupRow(80f, 0.1f, 0.8f, 0.1f),

                new GroupRow(10f, 1f)
        );

        buttonFactory = new AbstractLocalFactory<SimpleDate, TimetableSelectScreen>(this) {
            @Override
            protected Button newButton(SimpleDate data) {
                return TextButtonFactory.make("BIG", data.toString(), false);
            }
        };

        {
            TextButton newBT = TextButtonFactory.make("BIG", "+");
            TextButton editBT = TextButtonFactory.make("BIG", "E");
            TextButton removeBT = TextButtonFactory.make("BIG", "-");

            newBT.addListener(new TextButtonFactory.UpListener(){
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    core.varsOf("selectDate").set("editDate", false);
                    if (timetable.size() > 0)
                        core.varsOf("selectDate").set("editedDate", timetable.get(timetable.size() - 1));

                    core.setScreen("selectDate");
                    editDate =  -1;
                }
            });

            editBT.addListener(new TextButtonFactory.UpListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    if (dates.hasSelected()) {
                        core.varsOf("selectDate").set("editDate", true);
                        editDate = dates.getSelectedIndex();
                        core.varsOf("selectDate").set("editedDate", dates.getSelected());
                        core.setScreen("selectDate");
                    }
                }
            });

            removeBT.addListener(new TextButtonFactory.UpListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    if (dates.hasSelected())
                        dates.remove();
                }
            });


            UI.setActor(newBT, 4,1);
            UI.setActor(editBT, 4, 3);
            UI.setActor(removeBT, 4, 5);
            buttonFactory.addActors(editBT, removeBT);
        }





        {
            TextButton button = TextButtonFactory.make("MEDIUM", "Отмена");
            button.addListener(new TextButtonFactory.UpListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    core.back();
                }
            });
            UI.setActor(button, 1, 1);
        }

        {
            TextButton button = TextButtonFactory.make("MEDIUM", "Подтвердить");
            button.addListener(new TextButtonFactory.UpListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    VariableSystem vars = core.returnValues();
                    vars.set("returnTimetable", true);
                    vars.set("timetable", timetable);
                    core.back();
                }
            });
            UI.setActor(button, 1, 3);
        }

        UI.setActor(LabelFactory.make("BIG_NO_B", "Звонки"),7, 0);
        dates = new ButtonList<>(timetable = new SharedList<SimpleDate>(new LinkedList<>()), buttonFactory);
        UI.setActor(ScrollPaneFactory.make("main", dates.getVL()), 6, 1);
    }





    @Override
    public void show() {
        super.show();

        if (vars.testAndSet("returnTime", false)){
            if (editDate == -1)
                timetable.add( (SimpleDate)vars.getValue("editedTime"));
            else
                timetable.set(editDate, (SimpleDate)vars.getValue("editedTime"));

            timetable.sort(new Comparator<SimpleDate>() {
                @Override
                public int compare(SimpleDate simpleDate, SimpleDate t1) {
                    return simpleDate.getTime() - t1.getTime();
                }
            });
        }
        if (vars.testAndSet("loadTimetable", false)) {
            timetable.clear();

            standart = vars.getValue("date");

            SharedList<SimpleDate> newTimetable = vars.getValue("timetable");

            if (newTimetable == null)
                newTimetable = DataAPI.getDay(standart).get("timetable");

            if (newTimetable == null)
                return;

            for (SimpleDate date : newTimetable) {
                timetable.add(date);
            }
        }
        buttonFactory.update();

    }
}
