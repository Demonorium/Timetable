package em.demonorium.timetable.Menu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

import java.util.Calendar;


import em.demonorium.timetable.BasicScreen;
import em.demonorium.timetable.Core;
import em.demonorium.timetable.Factory.LabelFactory;
import em.demonorium.timetable.Factory.ScrollPaneFactory;
import em.demonorium.timetable.Factory.TextButtonFactory;
import em.demonorium.timetable.TimeData.DataAPI;
import em.demonorium.timetable.TimeData.DataBase.Entry;
import em.demonorium.timetable.TimeData.Models.DayModel;
import em.demonorium.timetable.TimeData.Models.LessonModel;
import em.demonorium.timetable.TimeData.Prototypes;
import em.demonorium.timetable.TimeData.SimpleDate;
import em.demonorium.timetable.Utils.AbstractLocalFactory;
import em.demonorium.timetable.Utils.AlignedButton;
import em.demonorium.timetable.Utils.AlignedGroup.AlignedGroup;
import em.demonorium.timetable.Utils.AlignedGroup.AlignedGroupConstructor;
import em.demonorium.timetable.Utils.AlignedGroup.GroupRow;
import em.demonorium.timetable.Utils.ButtonList;
import em.demonorium.timetable.Utils.SharedCollection.Default.SharedList;
import em.demonorium.timetable.Utils.SharedCollection.Editable;
import em.demonorium.timetable.Utils.VariableSystem.Action;
import em.demonorium.timetable.Utils.VariableSystem.VariableSystem;

public class DayEditScreen extends BasicScreen {
    public static boolean mode;
    private boolean localMode;
    private TextButton resetTimetable;
    private TextButton resetBT;
    private TextButton backBT;
    private Editor ed;
    private TextButton acceptBT;

    private static class Editor implements Editable<Integer, LessonModel> {
        private final DayEditScreen screen;

        private Editor(DayEditScreen screen) {
            this.screen = screen;
        }

        @Override
        public void added(Integer integer, LessonModel data) {
            screen.dayChanged();
        }

        @Override
        public void removed(Integer integer) {
            screen.dayChanged();
        }

        @Override
        public void cleared() {
            screen.dayChanged();
        }

        @Override
        public void updated(Integer integer) {
            screen.dayChanged();
        }
    }

    public DayEditScreen(Core core) {
        super(core, core.colors.getBasicColor("base"));

    }



    private ButtonList<LessonModel> lessonList;

    private DayModel day;
    private Label date;

    private static String getDate(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR);
    }

    private void dateChanged() {

        if (localMode) {
            day.fromEntry((Entry) vars.getValue("day"));
        } else {
            day.fromEntry(DataAPI.getDay(DataAPI.getSelectedDate()));
            date.setText(getDate(DataAPI.getSelectedDate()));
        }

        resetTimetable.setDisabled(day.standard);


        dayChanged_v = false;
        acceptBT.setDisabled(true);
        resetBT.setDisabled(true);
        factory.update();
    }

    private boolean dayChanged_v = false;
    private void dayChanged() {
        dayChanged_v = true;
        newLesson.setDisabled(!day.mayInsertLesson());

        resetBT.setDisabled(false);
        acceptBT.setDisabled(false);

        factory.update();
    }


    public static class LessonFactory extends AbstractLocalFactory<LessonModel, DayEditScreen> {
        LessonFactory(DayEditScreen dayEditScreen) {
            super(dayEditScreen);
        }

        @Override
        protected void onSelected() {
            Source.upperLesson.setDisabled(this.group.getSelectedIndex() <= 0);
            Source.downLesson.setDisabled(
                    (group.getSelectedIndex() >= (group.size() - 1))
                    && !Source.day.mayInsertLesson());
        }

        @Override
        public Button get(LessonModel data) {
            if (data.isSpace()) {
                Button button = TextButtonFactory.make("SPACE", null, null, Core.fontSettings.main.regular.getCapHeight()*2.3f);
                button.addListener(new TextButtonFactory.UpListener(){
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        boolean hasSelected = group.hasSelected();
                        update();
                        if (hasSelected)
                            onSelected();
                        else
                            onUnselected();
                    }
                });
                return button;
            }
            return super.get(data);
        }

        @Override
        public Button newButton(LessonModel data) {
            AlignedButton button = TextButtonFactory.make("BIG", Core.fontSettings.main.regular.getCapHeight()*2.3f, new AlignedGroup(
                    new GroupRow(1f, 1f),

                    new GroupRow(10f,   0.1f, 0.6f, 0.05f, 0.25f, 0.05f),
                    new GroupRow(1f,    0.1f),
                    new GroupRow(4f,   0.1f, 0.6f, 0.05f, 0.25f, 0.05f),

                    new GroupRow(1f, 1f)
            ));

            int id = Source.day.lessons.keyOf(data);

            {
                Label label = LabelFactory.make("INFO_NO_B",
                        Source.day.timetable.get(Prototypes.Day.getStart(id)).toString()
                                + " - "
                                + Source.day.timetable.get(Prototypes.Day.getEnd(id)).toString());

                label.setAlignment(Align.left);
                button.setActor(label, 1, 1);
            }

            {
                Label label = LabelFactory.make("MEDIUM_NO_B", (String)DataAPI.object.get(data.subject).get("name"));
                label.setAlignment(Align.left);
                button.setActor(label, 3, 1);
            }

            {
                button.setActor(LabelFactory.make("INFO_NO_B", data.auditory), 3,3);
                button.setActor(LabelFactory.make("INFO_NO_B", data.building), 1,3);
            }


            return button;
        }
    }
    private LessonFactory factory;
    private TextButton upperLesson, downLesson, removeLesson, newLesson, editLesson;

    @Override
    public void create() {
        localMode = mode;

        removeLesson = TextButtonFactory.make("BIG", "-");

        upperLesson = TextButtonFactory.make("BIG", "U");
        downLesson = TextButtonFactory.make("BIG", "D");

        day = new DayModel();
        factory = new LessonFactory(this);

        lessonList = new ButtonList<>(day.lessons, factory);

        {

            removeLesson.addListener(new TextButtonFactory.UpListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    if (lessonList.hasSelected()) {
                        VariableSystem localVars = core.varsOf("listYN");
                        localVars.set("what", "Удалить?");
                        localVars.set("yes", new Action() {
                            @Override
                            public void action() {
                                lessonList.remove();
                                dayChanged();
                            }
                        });
                        localVars.set("no", Action.NONE_ACTION);

                        core.setScreen("listYN");
                    }
                }
            });




            newLesson = TextButtonFactory.make("BIG", "+");
            newLesson.addListener(new TextButtonFactory.UpListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    if (newLesson.isDisabled()) return;

                    VariableSystem localVars;


                    localVars = core.varsOf("lessonEdit");
                    localVars.set("lessonList", lessonList);
                    localVars.setFlag("isLessonEdit", false);


                    localVars = core.varsOf("subSelect");
                    localVars.setFlag("isSelect");
                    localVars.setFlag("hasNext");
                    localVars.set("next", "lessonEdit");

                    core.setScreen("subSelect");
                }
            });

            editLesson = TextButtonFactory.make("BIG", "E");
            editLesson.addListener(new TextButtonFactory.UpListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    if (editLesson.isDisabled()) return;

                    VariableSystem localVars;


                    localVars = core.varsOf("lessonEdit");
                    localVars.set("lessonList", lessonList);
                    localVars.setFlag("isLessonEdit", true);
                    localVars.set("editLesson", lessonList.getSelectedIndex());


                    core.setScreen("lessonEdit");
                }
            });
        }


        TextButton editTimetable = TextButtonFactory.make("MEDIUM", "Изм. звонки");
        editTimetable.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                VariableSystem localVars = core.varsOf("timetableEdit");

                localVars.set("timetable", day.timetable);
                localVars.set("date", DataAPI.getSelectedDate());
                localVars.setFlag("loadTimetable");

                core.setScreen("timetableEdit");
            }
        });

        resetTimetable = TextButtonFactory.make("MEDIUM", "Сбросить звонки");
        resetTimetable.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (resetTimetable.isDisabled()) return;

                SharedList<SimpleDate> timetable = DataAPI.getStandardTimetable();
                day.applyTimetable(timetable);
                day.standard = true;
                resetTimetable.setDisabled(true);
                lessonList.update();
            }
        });



        ScrollPane basePane = ScrollPaneFactory.make("main", lessonList.getVL());





        upperLesson.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (lessonList.hasSelected()) {
                    int index = lessonList.getSelectedIndex();
                    if (index > 0) {
                        lessonList.swap(index - 1, index);
                        dayChanged();
                    }
                }


            }
        });


        downLesson.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (lessonList.hasSelected()) {
                    int index = lessonList.getSelectedIndex();
                    if (index < (lessonList.size() - 1)) {
                        lessonList.swap(index, index + 1);
                    } else {
                        if (!newLesson.isDisabled())
                            lessonList.add(lessonList.size() - 1, new LessonModel());
                    }
                    dayChanged();
                }
            }
        });


        acceptBT = TextButtonFactory.make("MEDIUM", "Принять");
        acceptBT.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (acceptBT.isDisabled()) return;

                if (dayChanged_v) {
                    if (localMode) {
                        core.returnValues().set("newDay", day);
                        core.returnValues().setFlag("returnDay");
                    } else {
                        DataAPI.addChanges(DataAPI.getSelectedDate(), day);
                    }
                }

                if (!localMode)
                    core.back();
                core.back();
            }
        });


        backBT = TextButtonFactory.make("MEDIUM", "Назад");
        backBT.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                if (dayChanged_v) {
                    VariableSystem localVars = core.varsOf("listYN");
                    localVars.set("what", "Вернуться?");
                    localVars.set("yes", new Action() {
                        @Override
                        public void action() {
                            core.back();
                            core.back();
                        }
                    });
                    localVars.set("no", Action.NONE_ACTION);

                    core.setScreen("listYN");
                } else {
                    core.back();
                    if (!localMode)
                        core.back();
                }

            }
        });

        resetBT = TextButtonFactory.make("MEDIUM", "Сбросить");
        resetBT.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (dayChanged_v) {
                    VariableSystem localVars = core.varsOf("listYN");
                    localVars.set("what", "Сбросить?");
                    localVars.set("yes", new Action() {
                        @Override
                        public void action() {
                            dayChanged_v = false;
                            dateChanged();
                        }
                    });
                    localVars.set("no", Action.NONE_ACTION);

                    core.setScreen("listYN");
                }
                resetBT.setDisabled(true);
            }
        });

        factory.addActors(removeLesson, upperLesson, downLesson, editLesson);
        AlignedGroupConstructor.RowConstructor lastRow = beginUI()
                .r(1f)
                .r(8f).c(0.1f).c(0.7f/3f, backBT).c(0.05f).c(0.7f/3f, resetBT).c(0.05f).c(0.7f/3f, acceptBT).c(0.1f)
                .r(1f)

                .r(5f)
                .r(8f).c(0.1f).c(0.35f, resetTimetable).c(0.1f).c(0.35f, editTimetable).c(0.1f)
                .r(1f)

                .r(2f)

                .r(8f).c(0.1f).c(0.2f - 0.15f/2f).c(0.2f, editLesson).c(0.2f- 0.15f/2f).c(0.05f).c(0.3f, downLesson).c(0.1f)
                .r(2f)
                .r(8f).c(0.1f).c(0.2f, newLesson).c(0.05f).c(0.2f, removeLesson).c(0.05f).c(0.3f, upperLesson).c(0.1f)

                .r(2f)

                .r(54f).c(0.1f).c(0.8f, basePane).c(0.1f)
                .r(1f)
                .r(8f)
                ;


        if (mode) {
            date = LabelFactory.make("BIG_NO_B", "Понедельник 1Н");
            lastRow.c(1f, date).r(1f);
            endUI();
        } else {

            TextButton upper = TextButtonFactory.make("BIG", "<");
            upper.addListener(new TextButtonFactory.UpListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    if (dayChanged_v) {
                        VariableSystem localVars = core.varsOf("listYN");
                        localVars.set("what", "Сбросить?");
                        localVars.set("yes", new Action() {
                            @Override
                            public void action() {
                                dayChanged_v = false;
                                DataAPI.leftDate();
                                dateChanged();
                            }
                        });
                        localVars.set("no", Action.NONE_ACTION);

                        core.setScreen("listYN");
                    } else {
                        DataAPI.leftDate();
                        dateChanged();
                    }

                }
            });



            date = LabelFactory.make("BIG", getDate(DataAPI.getSelectedDate()));



            TextButton down = TextButtonFactory.make("BIG", ">");
            down.addListener(new TextButtonFactory.UpListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    if (dayChanged_v) {
                        VariableSystem localVars = core.varsOf("listYN");
                        localVars.set("what", "Сбросить?");
                        localVars.set("yes", new Action() {
                            @Override
                            public void action() {
                                DataAPI.rightDate();
                                dateChanged();
                                dayChanged_v = false;
                            }
                        });
                        localVars.set("no", Action.NONE_ACTION);

                        core.setScreen("listYN");
                    } else {
                        DataAPI.rightDate();
                        dateChanged();
                    }
                }
            });

            lastRow.c(0.1f).c(0.1f, upper).c(0.05f).c(0.5f, date).c(0.05f).c(0.1f, down).c(0.1f)
            .r(1f);
            endUI();
        }
        ed = new Editor(this);
        this.lessonList.register(ed);
    }


    @Override
    public void show() {
        super.show();

        if (vars.testAndSet("loadDay", false)) {
            if (localMode) {
                date.setText((String) vars.getValue("currentDay"));
                day.fromEntry((Entry) vars.getValue("day"));
            } else {
                date.setText(getDate(DataAPI.getSelectedDate()));
                day.fromEntry(DataAPI.getSelectedDay());
            }

            dateChanged();
        }


        upperLesson.setDisabled(!lessonList.hasSelected());
        downLesson.setDisabled(!lessonList.hasSelected());
        removeLesson.setDisabled(!lessonList.hasSelected());
        editLesson.setDisabled(lessonList.hasSelected());



        if (vars.testAndSet("returnTimetable", false)) {
            SharedList<SimpleDate> dates = vars.getValue("timetable");
            day.applyTimetable(dates);
            day.standard = false;

            lessonList.update();
            dayChanged();
        }

        newLesson.setDisabled(!day.mayInsertLesson());
        resetTimetable.setDisabled(day.standard);

        factory.update();
    }
}
