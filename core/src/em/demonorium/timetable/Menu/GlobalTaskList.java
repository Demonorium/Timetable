package em.demonorium.timetable.Menu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import java.util.Calendar;
import java.util.GregorianCalendar;

import em.demonorium.timetable.BasicScreen;
import em.demonorium.timetable.Core;
import em.demonorium.timetable.Factory.LabelFactory;
import em.demonorium.timetable.Factory.TextButtonFactory;
import em.demonorium.timetable.TimeData.DataAPI;
import em.demonorium.timetable.TimeData.DataBase.Entry;
import em.demonorium.timetable.Utils.SharedCollection.Default.SharedList;
import em.demonorium.timetable.Utils.SharedCollection.SharedCollection;

public class GlobalTaskList extends BasicScreen {

    private Label label;

    public GlobalTaskList(Core _core) {
        super(_core);
    }

    private VerticalGroup todayList, weekList, prevList, afterList;
    private GregorianCalendar date;

    private static String getDate(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR);
    }
    @Override
    public void show() {
        super.show();

        if (vars.testAndSet("currentDate", false)) {
            date = (GregorianCalendar)Core.getCurrentDate().clone();

            dayChanged();
        }

        if (vars.testAndSet("loadDate", false)) {
            date = (GregorianCalendar)((GregorianCalendar) (vars.getValue("date"))).clone();
            dayChanged();
        }
    }


    private static void addDay(GregorianCalendar cal) {
        cal.add(Calendar.DAY_OF_YEAR, 1);
    }

    public void dayChanged() {
        if (date.get(Calendar.DAY_OF_YEAR) == Core.getCurrentDate().get(Calendar.DAY_OF_YEAR)) {
            label.setText("СЕГОДНЯ");
        } else {
            label.setText("На " + getDate(date));
        }

        todayList.clear();
        prevList.clear();
        afterList.clear();
        weekList.clear();

        SharedCollection<GregorianCalendar, SharedList<Entry>> collection = DataAPI.object.get("tasks");
        //Заполнение списка на завтра

        GregorianCalendar calendar = (GregorianCalendar)date.clone();

        {
            addDay(calendar);
            SharedList<Entry> day = collection.get(calendar);
            if (day != null) {
                for (Entry task : day) {
                    String name = task.get("name");

                    TextButton taskButton = TextButtonFactory.make("MEDIUM", name);
                    todayList.addActor(taskButton);
                }
            }
        }

        for (int i = 1; i < 7; ++i) {
            addDay(calendar);
            SharedList<Entry> day = collection.get(calendar);
            if (day != null) {
                for (Entry task: day) {
                    String name = task.get("name");

                    TextButton taskButton = TextButtonFactory.make("MEDIUM", name);
                    weekList.addActor(taskButton);
                }
            }
        }
        addDay(calendar);



    }

    @Override
    public void create() {
        label = LabelFactory.make("BIG", "Задания");
        Image backNoB = new Image(core.colors.getColor("base"));

        TextButton backBT = TextButtonFactory.make("BIG", "Назад");
        backBT.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                core.back();
            }
        });

        VerticalGroup totalG = new VerticalGroup();
        ScrollPane pane = new ScrollPane(totalG);

        totalG.grow();

        totalG.addActor(LabelFactory.make("BAD", "ДЕДЛАЙН"));
        todayList = new VerticalGroup();
        totalG.addActor(todayList);

        totalG.addActor(LabelFactory.make("NORMAL", "На неделе"));
        weekList = new VerticalGroup();
        totalG.addActor(weekList);

        totalG.addActor(LabelFactory.make("BIG_BOLD", "Долгосрочное"));
        afterList = new VerticalGroup();
        totalG.addActor(afterList);

        totalG.addActor(LabelFactory.make("GOOD", "Сдано"));
        prevList = new VerticalGroup();
        totalG.addActor(prevList);


        todayList.grow();
        prevList.grow();
        afterList.grow();
        weekList.grow();

        beginUI()
                .r(1f).c(1f, backNoB, -1)
                .r(8f).c(0.1f).c(0.8f, backBT).c(0.1f)
                .r(1f).c(1f, backNoB, -1)

                .r(80f).c(1f, pane)

                .r(10f).c(1f, label)
                ;
        endUI();
    }
}
