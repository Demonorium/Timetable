package em.demonorium.timetable.Menu;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.Calendar;
import java.util.GregorianCalendar;

import em.demonorium.timetable.BasicScreen;
import em.demonorium.timetable.Core;
import em.demonorium.timetable.Factory.ContainerFactory;
import em.demonorium.timetable.Factory.LabelFactory;
import em.demonorium.timetable.Factory.Locale;
import em.demonorium.timetable.Factory.ScrollPaneFactory;
import em.demonorium.timetable.Factory.TextButtonFactory;
import em.demonorium.timetable.TimeData.DataAPI;
import em.demonorium.timetable.TimeData.DataBase.Entry;
import em.demonorium.timetable.TimeData.Prototypes;
import em.demonorium.timetable.TimeData.SimpleDate;
import em.demonorium.timetable.Utils.AlignedButton;
import em.demonorium.timetable.Utils.AlignedGroup.ActorSize;
import em.demonorium.timetable.Utils.AlignedGroup.AlignedGroup;
import em.demonorium.timetable.Utils.AlignedGroup.AlignedGroupConstructor;
import em.demonorium.timetable.Utils.AlignedGroup.GroupRow;
import em.demonorium.timetable.Utils.SharedCollection.Default.SharedList;
import em.demonorium.timetable.Utils.SharedCollection.SharedCollection;
import em.demonorium.timetable.Utils.VariableSystem.VariableSystem;

public class MainScreen extends BasicScreen {
    public MainScreen(Core core) {
        super(core);
    }

    private Label DAY_LABEL, WEEK_LABEL;
    private TextButton MONTH_BUTTON;

    private HorizontalGroup horizontalGroup;
    private ScrollPane horizontalGroupPane;

    private float scrollPosition = 0f;
    private static final float TIME_COUNT_DOWN = 1.5f;
    private float timeBeforeScroll = 0f;

    private TextButton.TextButtonStyle taskStyle;


    private static String getDay(Calendar day) {
        String str = Integer.toString(day.get(Calendar.DAY_OF_MONTH));
        if (str.length() == 1)
            str = "0" + str;
        return str;
    }

    private static boolean hasOnWeek(GregorianCalendar day) {
        SharedCollection<GregorianCalendar, SharedList<Entry>> week = DataAPI.object.get("tasks");
        GregorianCalendar calendar =  (GregorianCalendar)day.clone();
        for (int i = 0; i < 6; ++i) {
            calendar.add(Calendar.DAY_OF_YEAR,1);
            SharedList<Entry> list = week.get(calendar);
            if (list == null)
                continue;;
            if (!list.isEmpty())
                return true;
        }
        return false;
    }
    private static boolean hasOnNextDay(GregorianCalendar day) {
        SharedCollection<GregorianCalendar, SharedList<Entry>> week = DataAPI.object.get("tasks");
        GregorianCalendar calendar =  (GregorianCalendar)day.clone();
        calendar.add(Calendar.DAY_OF_YEAR,1);
        SharedList<Entry> list = week.get(calendar);
        if (list == null)
            return false;

        return !list.isEmpty();
    }

    private static String getDayOfWeek(GregorianCalendar day) {
        String str = Integer.toString(DataAPI.getDayOfWeek(day));
        if (str.length() == 1)
            str = "0" + str;
        return str;
    }

    private static String getMonth(Calendar month) {
        String str = Integer.toString(month.get(Calendar.MONTH));
        if (str.length() == 1)
            str = "0" + str;
        return Locale.get("M" + str);
    }


    private String getWeek(GregorianCalendar week) {
        return (DataAPI.getWeekNumber(week) + 1) + "Н";
    }


    private void dateUpdated() {
        leftContainer.applyDay(DataAPI.getPrevDate());
        centerContainer.applyDay(DataAPI.getSelectedDate());
        rightContainer.applyDay(DataAPI.getNextDate());

        DAY_LABEL.setText(getDay(Core.getCurrentDate()));
        MONTH_BUTTON.setText(getMonth(Core.getCurrentDate()));
        WEEK_LABEL.setText(getWeek(Core.getCurrentDate()));
    }

    private boolean first = true;
    private static final float speed = 0.25f;
    @Override
    public void render(float delta) {
        super.render(delta);

        if (timeBeforeScroll < delta) {
            timeBeforeScroll = 0f;
            if (first) {
                horizontalGroupPane.setScrollX(scrollPosition);
                first = false;
            }
            float currentSpeed = speed * delta;
            float currentDelta = horizontalGroupPane.getScrollPercentX() - 0.5f;

            if (Math.abs(currentDelta) < currentSpeed) {
                horizontalGroupPane.setScrollPercentX(0.5f);
            } else {
                horizontalGroupPane.setScrollPercentX(horizontalGroupPane.getScrollPercentX()  -
                        Math.signum(currentDelta)*currentSpeed);
            }

            horizontalGroupPane.setSmoothScrolling(true);
        } else {
            if ((horizontalGroupPane.getVelocityX() < 0.1f))
                timeBeforeScroll -= delta;

            if (horizontalGroupPane.getScrollPercentX() < 0.2f) {
                DataAPI.leftDate();
                dateUpdated();

                horizontalGroupPane.setScrollPercentX(horizontalGroupPane.getScrollPercentX() + 0.5f);
            } else if (horizontalGroupPane.getScrollPercentX() > 0.8f) {
                DataAPI.rightDate();
                dateUpdated();

                horizontalGroupPane.setScrollPercentX(horizontalGroupPane.getScrollPercentX() - 0.5f);
            }
        }
    }

    @Override
    public void create() {
        //Кнопка редактировать

        Image editBT_BACK = new Image(core.colors.getColor("base"));
        TextButton editBT = TextButtonFactory.make("BIG", "Редактировать");
        editBT.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                core.varsOf("dayedit").setFlag("loadDay");
                core.setScreen("select");
            }
        });

        //Центр
        horizontalGroup = new HorizontalGroup();
        horizontalGroupPane = new ScrollPane(horizontalGroup);
        horizontalGroupPane.setOverscroll(false, false);
        horizontalGroupPane.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                timeBeforeScroll = TIME_COUNT_DOWN;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                timeBeforeScroll = TIME_COUNT_DOWN;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
                timeBeforeScroll = TIME_COUNT_DOWN;
            }
        });

        leftContainer = new DayContainer(DataAPI.getPrevDate());
        centerContainer = new DayContainer(DataAPI.getSelectedDate());
        rightContainer = new DayContainer(DataAPI.getNextDate());

        horizontalGroup.addActor(leftContainer.container);
        horizontalGroup.addActor(centerContainer.container);
        horizontalGroup.addActor(rightContainer.container);


        DAY_LABEL = LabelFactory.make("BIG_NO_B", getDay(Core.getCurrentDate()));
        DAY_LABEL.setAlignment(Align.center);

        WEEK_LABEL = LabelFactory.make("BIG_NO_B", getWeek(Core.getCurrentDate()));
        WEEK_LABEL.setAlignment(Align.center);

        MONTH_BUTTON = TextButtonFactory.make("BIG", getMonth(Core.getCurrentDate()));
        MONTH_BUTTON.getLabel().setAlignment(Align.left);
        MONTH_BUTTON.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                DataAPI.selectDate((GregorianCalendar) Core.getCurrentDate().clone());
                dateUpdated();
            }
        });

        Container monthBox = core.containerFactory.get(MONTH_BUTTON, "front", Align.left);
        monthBox.pad(5);


        ContainerFactory.BoxContainer<Label> dayBox = core.containerFactory.getBox(DAY_LABEL);
        dayBox.setInBackground(core.colors.getColor("front"));

        ContainerFactory.BoxContainer<Label> weekBox = core.containerFactory.getBox(WEEK_LABEL);
        weekBox.setInBackground(core.colors.getColor("front"));

        TextButtonFactory.UpListener<Core> taskListener = new TextButtonFactory.UpListener<Core>(core) {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                core.varsOf("taskList").setFlag("currentDate");
                core.setScreen("taskList");
            }
        };

        taskStyle = new TextButton.TextButtonStyle();
        taskStyle.font = Core.fontSettings.main.bold;
        taskStyle.fontColor = core.colors.getBasicColor("mainFont");
        taskStyle.up = core.colors.getColor("bad");


        TextButton taskBt = new TextButton("!", taskStyle);
        taskBt.addListener(taskListener);

        ContainerFactory.BoxContainer<TextButton> taskBox = core.containerFactory.getBox(taskBt);

        Image topO = new Image(core.colors.getColor("base"));

        beginUI()
                .r(1f).c(1f, editBT_BACK, -1)
                .r(8f).c(0.1f).c(0.8f, editBT).c(0.1f)
                .r(1f).c(1f, editBT_BACK, -1)

                .r(80f).c(1f, horizontalGroupPane)

                .r(1f).c(1f,topO,-1)

                .r(1f).c(1f).c(15f, dayBox).c(84f)
                .r(6f).c(1f).c(15f).c(3f).c(45f, monthBox).c(1f).c(15f, weekBox).c(4f).c(20f, taskBox).c(1f)
                .r(1f).c(1f).c(15f, dayBox).c(84f)

                .r(1f).c(1f,topO,-1)
                ;
        endUI();
    }



    private class DayContainer {
        private Label DAY;
        private Label MONTH;
        private Label WEEK;
        private Label DAY_OF_WEEK;

        private TextButton TASK;
        private TextButton.TextButtonStyle TAKS_ST;
        private TextButton ttBT;


        VerticalGroup lessons;


        AlignedGroup group;

        Container container;


        GregorianCalendar date;

        boolean thisDay(Calendar day) {
            return day.get(Calendar.DAY_OF_YEAR) == Core.getCurrentDate().get(Calendar.DAY_OF_YEAR);
        }

        DayContainer(GregorianCalendar day) {
            DAY = LabelFactory.make("BIG",      getDay(day));
            MONTH = LabelFactory.make("BIG",    getMonth(day));
            WEEK = LabelFactory.make("BIG",     getWeek(day));
            DAY_OF_WEEK = LabelFactory.make("BIG", getDayOfWeek(day));


            TAKS_ST = new TextButton.TextButtonStyle();
            TAKS_ST.font = Core.fontSettings.main.bold;
            TAKS_ST.fontColor = core.colors.getBasicColor("mainFont");
            TAKS_ST.up = core.colors.getColor("bad");

            TASK = new TextButton("!", TAKS_ST);
            TASK.addListener(new TextButtonFactory.UpListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    core.varsOf("taskList").setFlag("loadDate");
                    core.varsOf("taskList").set("date", DataAPI.getSelectedDate());
                    core.setScreen("taskList");
                }
            });
            DAY.setAlignment(Align.center);
            MONTH.setAlignment(Align.left);
            WEEK.setAlignment(Align.center);


            Container monthBox = core.containerFactory.get(MONTH, "front", Align.left);
            monthBox.pad(5);

            ContainerFactory.BoxContainer<Label> dayBox = core.containerFactory.getBox(DAY);
            dayBox.setInBackground(core.colors.getColor("front"));

            ContainerFactory.BoxContainer<Label> weekBox = core.containerFactory.getBox(WEEK);
            weekBox.setInBackground(core.colors.getColor("front"));

            ContainerFactory.BoxContainer<Label> dayOfWeekBox = core.containerFactory.getBox(DAY_OF_WEEK);
            dayOfWeekBox.setInBackground(core.colors.getColor("front"));

            ttBT = TextButtonFactory.make("BIG", "Расписание");
            ttBT.getLabel().setAlignment(Align.center);
            ttBT.addListener(new TextButtonFactory.UpListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    DataAPI.selectDate(date);
                    core.varsOf("dayedit").setFlag("loadDay");
                    core.setScreen("select");
                    core.setScreen("dayedit");
                }
            });



            lessons = new VerticalGroup();
            lessons.grow();

            ScrollPane pane = ScrollPaneFactory.make("main", lessons);


            lessons.space(4f);
            ContainerFactory.BoxContainer<TextButton> taskBox = core.containerFactory.getBox(TASK);

            group = new AlignedGroupConstructor()
                    .r(2f)
                    .r(79f*0.9f).c(2f).c(98f, pane).c(2f)

                    .r(2f)
                    .r(9f).c(2f).c(63f, ttBT).c(1f).c(15f, dayOfWeekBox).c(19f).c(2f)
                    .r(2f)
                    .r(0.5f).c(2f).c(15f, dayBox).c(3f).c(45f, monthBox).c(1f).c(15f, weekBox).c(4f).c(15f).c(2f)
                    .r(8f).c(2f).c(15f).c(3f).c(45f).c(1f).c(15f).c(4f).c(15f, taskBox).c(2f)
                    .r(0.5f).c(2f).c(15f, dayBox).c(3f).c(45f, monthBox).c(1f).c(15f, weekBox).c(4f).c(15f).c(2f)
                    .r(2f)

                    .getGroup();

            container = new Container<>(group);
            container.setBackground(core.colors.getColor("base"));



            applyDay(day);

        }


        void applyDay(GregorianCalendar _date) {
            date = _date;
            if (thisDay(_date)) {
                DAY.setText("NN");
                MONTH.setText("Сегодня");
                WEEK.setText("NN");
            } else {
                DAY.setText(getDay(_date));
                MONTH.setText(getMonth(_date));
                WEEK.setText(getWeek(_date));
            }
            DAY_OF_WEEK.setText(Locale.get("D"+getDayOfWeek(_date)));
            this.lessons.clear();

            if (hasOnNextDay(date)) {
                TAKS_ST.up = core.colors.getColor("bad");
            } else if (hasOnWeek(date)) {
                TAKS_ST.up = core.colors.getColor("normal");
            } else {
                TAKS_ST.up = core.colors.getColor("front");
            }


            Entry day = DataAPI.getDay(_date);
            if (day != null) {
                SharedList<Entry> lessons = day.get("lessons");
                SharedList<SimpleDate> timetable = day.get("timetable");
                if ((lessons != null) && (timetable != null)) {
                    for (int i = 0; i < lessons.size(); ++i) {
                        final Entry data = lessons.get(i);

                        if (Prototypes.Lesson.isSpace(data)) {
                            this.lessons.addActor(TextButtonFactory.make("SPACE", null, null, Core.fontSettings.main.regular.getCapHeight()*2.3f));
                        } else {
                            AlignedButton button = TextButtonFactory.make("BIG", Core.fontSettings.main.regular.getCapHeight() * 2.3f, new AlignedGroup(
                                    new GroupRow(1f, 1f),

                                    new GroupRow(10f, 0.1f, 0.6f, 0.05f, 0.25f, 0.05f),
                                    new GroupRow(1f, 0.1f),
                                    new GroupRow(4f, 0.1f, 0.6f, 0.05f, 0.25f, 0.05f),

                                    new GroupRow(1f, 1f)
                            ));

                            int id = lessons.keyOf(data);

                            {
                                Label label = LabelFactory.make("INFO_NO_B",
                                        timetable.get(Prototypes.Day.getStart(id)).toString()
                                                + " - "
                                                + timetable.get(Prototypes.Day.getEnd(id)).toString());

                                label.setAlignment(Align.left);
                                button.setActor(label, 1, 1);
                            }

                            {
                                Label label = LabelFactory.make("MEDIUM_NO_B",
                                        (String)((Entry)DataAPI.base().get((Integer)data.get("subject"))).get("name"));
                                label.setAlignment(Align.left);
                                button.setActor(label, 3, 1);
                            }

                            {
                                button.setActor(LabelFactory.make("INFO_NO_B", (String)data.get("auditory")), 3, 3);
                                button.setActor(LabelFactory.make("INFO_NO_B", (String)data.get("building")), 1, 3);
                            }


                            button.addListener(new TextButtonFactory.UpListener<Bundle>(new Bundle(this, data)) {
                                @Override
                                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                    VariableSystem localVars = core.varsOf("subjectMenu");
                                    localVars.setFlag("loadDate");
                                    localVars.set("date", object.table.date);

                                    localVars.setFlag("loadLesson");
                                    localVars.set("lesson", object.lesson);

                                    Entry localDay = DataAPI.getDay(object.table.date);
//                                    if (DataAPI.getChanges().containsValue(localDay)) {
//                                        localVars.setFlag("changes");
//                                    }

                                    localVars.set("lessonID", ((SharedList<Entry>)localDay.get("lessons")).keyOf(object.lesson));
                                    core.setScreen("subjectMenu");
                                }
                            });
                            this.lessons.addActor(button);
                        }
                    }
                }
            }
        }
    }

    private static class Bundle {
        DayContainer table;
        Entry lesson;
        Entry subject;
        int subjectID;

        public Bundle(DayContainer table, Entry lesson) {
            this.table = table;
            this.lesson = lesson;
            this.subjectID = lesson.get("subject");
            this.subject = DataAPI.base().get(subjectID);
        }
    }

    private DayContainer leftContainer, centerContainer, rightContainer;

    @Override
    public void show() {
        super.show();

        dateUpdated();
        timeBeforeScroll = 0f;

        if (hasOnNextDay(Core.getCurrentDate())) {
            taskStyle.up = core.colors.getColor("bad");
        } else if (hasOnWeek(Core.getCurrentDate())) {
            taskStyle.up = core.colors.getColor("normal");
        } else {
            taskStyle.up = core.colors.getColor("front");
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);



        leftContainer.container.minWidth(width*0.9f);
        centerContainer.container.minWidth(width*0.9f);
        rightContainer.container.minWidth(width*0.9f);

        leftContainer.container.maxWidth(width*0.9f);
        centerContainer.container.maxWidth(width*0.9f);
        rightContainer.container.maxWidth(width*0.9f);

        leftContainer.container.minHeight(horizontalGroupPane.getHeight()*0.98f);
        centerContainer.container.minHeight(horizontalGroupPane.getHeight()*0.98f);
        rightContainer.container.minHeight(horizontalGroupPane.getHeight()*0.98f);

        horizontalGroup.space(0.1f * width);
        scrollPosition = (width *(0.95f));
    }
}
