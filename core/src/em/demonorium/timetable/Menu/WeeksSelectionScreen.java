package em.demonorium.timetable.Menu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.ArrayList;
import java.util.List;

import em.demonorium.timetable.BasicScreen;
import em.demonorium.timetable.Core;
import em.demonorium.timetable.Factory.LabelFactory;
import em.demonorium.timetable.Factory.Locale;
import em.demonorium.timetable.Factory.ScrollPaneFactory;
import em.demonorium.timetable.Factory.TextButtonFactory;
import em.demonorium.timetable.TimeData.DataAPI;
import em.demonorium.timetable.TimeData.DataBase.Entry;
import em.demonorium.timetable.TimeData.Models.DayModel;
import em.demonorium.timetable.TimeData.Prototypes;
import em.demonorium.timetable.TimeData.SimpleDate;
import em.demonorium.timetable.Utils.FixedButton;
import em.demonorium.timetable.Utils.SharedCollection.Default.SharedList;
import em.demonorium.timetable.Utils.VariableSystem.VariableSystem;

public class WeeksSelectionScreen extends BasicScreen {
    public WeeksSelectionScreen(Core _core) {
        super(_core, _core.colors.getBasicColor("base"));
    }


    private TextButton editDayBT;
    private TextButton removeWeek, selectWeek;

    private ButtonGroup<Button> weekGroup, dayGroup;


    private HorizontalGroup weeksList;
    private ScrollPane weeksPane;
    private HorizontalGroup daysList;

    private boolean weekSelected() {
        return weekGroup.getCheckedIndex() != -1;
    }

    private int currentWeek() {
        return weeksList.getChildren().indexOf(weekGroup.getChecked(), true);
    }

    private int selectedDayIndex() {
        return dayGroup.getCheckedIndex();
    }
    private Entry selectedDay() {
        return DataAPI.getWeeks().get(currentWeek()).get(selectedDayIndex());
    }

    private void applyWeek(int week) {
        selectWeek.setDisabled(DataAPI.getWeekNumber(Core.getCurrentDate()) == week);

        ArrayList<Entry> wk = DataAPI.getWeeks().get(week);
        int cnt = 0;

        daysList.clear();
        dayGroup.clear();

        for (Entry day: wk) {
            FixedButton<Label> newDayButton = TextButtonFactory.make(Prototypes.Day.isHoliday(day) ? "GOOD": "BIG",
                    LabelFactory.make("BIG_NO_B", Locale.get("D0"+cnt)),
                    weeksPane.getHeight(), null, false);

            daysList.addActor(newDayButton);
            dayGroup.add(newDayButton);

            ++cnt;
        }


    }

    abstract static class LST extends TextButtonFactory.UpListener {
        int week;

        LST(int week) {
            this.week = week;
        }
    }

    public void update() {
        weeksList.clear();
        weekGroup.clear();
        daysList.clear();
        dayGroup.clear();

        if (DataAPI.getWeeksCount() > 0) {
            ArrayList<ArrayList<Entry>> weeks = DataAPI.getWeeks();
            for (int i = 0; i < weeks.size(); ++i) {
                FixedButton<Label> newWeekButton = TextButtonFactory.make("BIG", LabelFactory.make("BIG_NO_B", SimpleDate.tToStr(i+1)),
                        weeksPane.getHeight(), null, false);
                newWeekButton.addListener(new LST(i) {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        applyWeek(week);
                    }
                });

                weeksList.addActor(newWeekButton);
                weekGroup.add(newWeekButton);
            }
            applyWeek(weekGroup.getCheckedIndex());
        }

        FixedButton<Label> newWeekButton = TextButtonFactory.make("BIG", LabelFactory.make("BIG_NO_B", "+"),
                weeksPane.getHeight(), null);

        newWeekButton.setWidth(Core.fontSettings.secondary.bold.getLineHeight()*2.35f);
        newWeekButton.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (DataAPI.getStandardTimetable().size() > 0) {
                    List<ArrayList<Entry>> weeks = DataAPI.getWeeks();

                    ArrayList<Entry> week = new ArrayList<>();
                    for (int i = 0; i < 7; ++i) {
                        week.add(DataAPI.base().newEntry(Prototypes.Day.prototype));
                    }
                    weeks.add(week);
                    update();
                }
            }
        });
        newWeekButton.setDisabled(DataAPI.getStandardTimetable().size() < 2);

        editDayBT.setDisabled(dayGroup.getChecked() == null);
        removeWeek.setDisabled(!weekSelected());
        selectWeek.setDisabled((!weekSelected()) || (DataAPI.getWeekNumber(Core.getCurrentDate()) == currentWeek()));

        weeksList.addActor(newWeekButton);
    }




    @Override
    public void show() {
        super.show();

        if (vars.testAndSet("returnTimetable", false)) {
            SharedList<SimpleDate> timetable = DataAPI.getStandardTimetable();
            timetable.clear();
            for (SimpleDate date: (SharedList<SimpleDate>)vars.getValue("timetable"))
                timetable.add(date);

            for (ArrayList<Entry> week: DataAPI.getWeeks()) {
                for (Entry day: week) {
                    if (day.get("isTimetableStandard"))
                        Prototypes.Day.setStandardTimetable(day, DataAPI.base());
                }
            }

            for (Entry day: DataAPI.getChanges())
                if (day.get("isTimetableStandard"))
                    Prototypes.Day.setStandardTimetable(day, DataAPI.base());
        }

        if (vars.testAndSet("returnDay", false)) {
            ((DayModel)vars.getValue("newDay")).toEntry(selectedDay());
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        update();
    }

    @Override
    public void create() {

        TextButton backBT = TextButtonFactory.make("BIG", "Назад");
        backBT.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                core.back();
                core.back();
            }
        });

        Label weekLB = LabelFactory.make("BIG_NO_B", "Неделя");
        final Label dayLB = LabelFactory.make("BIG_NO_B", "День");

        weekGroup = new ButtonGroup<>();
        dayGroup = new ButtonGroup<>();

        TextButton standardTimetableBT = TextButtonFactory.make("MEDIUM", "Стандартное расписание звонков");
        standardTimetableBT.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                VariableSystem localVars = core.varsOf("timetableEdit");

                localVars.set("timetable", DataAPI.getStandardTimetable());
                localVars.set("date", DataAPI.getSelectedDate());
                localVars.setFlag("loadTimetable");

                core.setScreen("timetableEdit");
            }
        });

        editDayBT = TextButtonFactory.make("MEDIUM", "Редактировать день");
        editDayBT.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (editDayBT.isDisabled()) return;

                VariableSystem localVars = core.varsOf("weekDayEdit");
                localVars.set("currentDay", Locale.get("D1"+selectedDayIndex()) + " " + (currentWeek()+1) + "Н");
                localVars.set("loadDay", true);
                localVars.set("day", selectedDay());

                core.setScreen("weekDayEdit");
            }
        });

        removeWeek = TextButtonFactory.make("MEDIUM", "Удал. неделю");
        removeWeek.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (weekSelected()) {
                    DataAPI.getWeeks().remove(currentWeek());
                    weeksList.removeActor(weekGroup.getChecked());
                    weekGroup.remove(weekGroup.getChecked());
                    update();
                }
            }
        });

        selectWeek = TextButtonFactory.make("MEDIUM", "Устан. неделю");
        selectWeek.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (weekSelected()) {
                    DataAPI.setWeek(currentWeek());
                    selectWeek.setDisabled(true);
                }
            }
        });


        weeksList = new HorizontalGroup();
        daysList = new HorizontalGroup();

        weeksList.space(3f);
        daysList.space(3f);

        weeksList.grow();
        daysList.grow();


        weeksPane = ScrollPaneFactory.make("main", weeksList);
        ScrollPane daysPane = ScrollPaneFactory.make("main", daysList);




        beginUI()
                .r(1f)
                .r(8f).c(.1f).c(0.8f, backBT).c(0.1f)
                .r(1f)

                .r(10f)

                .r(10f).c(.1f).c(0.8f, standardTimetableBT).c(0.1f)

                .r(10f)
                .r(10f).c(.1f).c(0.8f, editDayBT).c(0.1f)

                .r(2.5f)
                .r(10f).c(.1f).c(0.35f, removeWeek).c(0.1f).c(0.35f, selectWeek).c(0.1f)

                .r(5f)

                .r(10f).c(1f).c(8f, daysPane).c(1f)
                .r(10f).c(1f).c(8f, dayLB).c(1f)

                .r(2.5f)

                .r(10f).c(1f).c(8f, weeksPane).c(1f)
                .r(10f).c(1f).c(8f, weekLB).c(1f)
                .r(1f)
        ;
        endUI();
    }
}
