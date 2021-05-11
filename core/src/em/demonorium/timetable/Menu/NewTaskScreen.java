package em.demonorium.timetable.Menu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import em.demonorium.timetable.BasicScreen;
import em.demonorium.timetable.Core;
import em.demonorium.timetable.Factory.LabelFactory;
import em.demonorium.timetable.Factory.Locale;
import em.demonorium.timetable.Factory.ScrollPaneFactory;
import em.demonorium.timetable.Factory.TextButtonFactory;
import em.demonorium.timetable.TimeData.DataAPI;
import em.demonorium.timetable.TimeData.DataBase.Entry;
import em.demonorium.timetable.TimeData.Models.NextLessonInfo;
import em.demonorium.timetable.TimeData.Models.TaskModel;
import em.demonorium.timetable.TimeData.SimpleDate;
import em.demonorium.timetable.TimeData.VisibleObject;
import em.demonorium.timetable.Utils.AbstractLocalFactory;
import em.demonorium.timetable.Utils.AlignedButton;
import em.demonorium.timetable.Utils.AlignedGroup.AlignedGroup;
import em.demonorium.timetable.Utils.AlignedGroup.AlignedGroupConstructor;
import em.demonorium.timetable.Utils.AlignedGroup.GroupRow;
import em.demonorium.timetable.Utils.ButtonList;
import em.demonorium.timetable.Utils.Input.KeyboardInputAdapter;
import em.demonorium.timetable.Utils.SharedCollection.Default.SharedList;
import em.demonorium.timetable.Utils.SharedCollection.SharedCollection;
import em.demonorium.timetable.Utils.VariableSystem.VariableSystem;

public class NewTaskScreen extends BasicScreen {

    private ButtonList<VisibleObject> description;
    private AbstractLocalFactory<VisibleObject, NewTaskScreen> descriptionFactory;
    private TaskModel model;


    private KeyboardInputAdapter nameInput;
    private Container<AlignedGroup> container;
    private SelectModel dateSelect;
    private Label typeLB;

    @Override
    public void show() {
        super.show();
        descriptionFactory.update();

        if (vars.testAndSet("loadDate", false)) {
            currentModel = DATE_MODEL;
            model.date = vars.getValue("date");
        }


        if (vars.testAndSet("subjectSelected", false)) {
            SubjectSelect select = (SubjectSelect)selectModels.get(currentModel);
            select.subjectID = vars.getValue("selectedSubject");
        }
        if (vars.testAndSet("loadSubject", false)) {
            currentModel = SUBJECT_MODEL;
            SubjectSelect select = (SubjectSelect)selectModels.get(currentModel);
            select.subjectID = vars.getValue("subject");
            select.prevSub = -9;
            select.shift = 1;
        }
        if (vars.testAndSet("returnVisible", false)) {
            model.content.add((VisibleObject)vars.getValue("visible"));
        }

        activateModel();
    }

    public NewTaskScreen(Core _core) {
        super(_core, _core.colors.getBasicColor("base"));
    }

    @Override
    public void create() {
        nameInput = core.inputFactory.get("main", "Задание");
        model = new TaskModel();
        container = new Container<>();
        container.fill();

        TextButton acceptBT, backBT;

        backBT = TextButtonFactory.make("MEDIUM", "Назад");
        backBT.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                core.back();
            }
        });

        acceptBT = TextButtonFactory.make("MEDIUM", "Создать");
        acceptBT.addListener(new TextButtonFactory.UpListener<NewTaskScreen>(this) {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                model.name = nameInput.getText();
                SharedCollection<GregorianCalendar, SharedList<Entry>> collection = DataAPI.object.get("tasks");
                SharedList<Entry> day = collection.get(model.date);
                if (day == null) {
                    day = new SharedList<>(new ArrayList<>());
                    collection.add(model.date, day);
                }
                day.add(model.toEntry());
                core.back();
            }
        });



        final TextButton newBT = TextButtonFactory.make("BIG", "+");
        newBT.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                core.setScreen("taskElement");
            }
        });
        final TextButton editBT = TextButtonFactory.make("BIG", "E");
        editBT.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (editBT.isDisabled()) return;
                core.setScreen("taskElement");
            }
        });
        final TextButton delBT = TextButtonFactory.make("BIG", "-");


        descriptionFactory = new DescriptionFactory(this);
        description = new ButtonList<>(model.content, descriptionFactory);
        final ScrollPane descrPane = ScrollPaneFactory.make("main", description.getVL());

        descriptionFactory.addActors(editBT, delBT);

        Label label = LabelFactory.make("MEDIUM_NO_B", "Название:");
        label.setAlignment(Align.right);

        typeLB = LabelFactory.make("BIG", "SB0");
        TextButton leftType = TextButtonFactory.make("BIG", "<");
        leftType.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                prevModel();
            }
        });

        TextButton rightType = TextButtonFactory.make("BIG", ">");
        rightType.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                nextModel();
            }
        });

        beginUI()
                .r(1f)
                .r(8f).c(.1f).c(0.35f, backBT).c(0.1f).c(0.35f, acceptBT).c(.1f)
                .r(1f)

                .r(1f)
                .r(8f).c(0.1f).c(0.2f, newBT).c(0.1f).c(0.2f, editBT).c(0.1f).c(0.2f, delBT).c(0.1f)
                .r(1f)

                .r(40f).c(0.1f).c(0.8f, descrPane).c(0.1f)

                .r(1f)
                .r(20f).c(0.1f).c(0.8f, container).c(0.1f)

                .r(1f)
                .r(8f).c(0.1f).c(0.1f, leftType).c(0.05f).c(0.5f, typeLB).c(0.05f).c(0.1f, rightType).c(0.1f)

                .r(1f)
                .r(8f).c(0.1f).c(0.15f, label).c(0.1f).c(0.55f, nameInput.getActor()).c(0.1f)
                .r(1f)
                ;
        endUI();


        editBT.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (editBT.isDisabled()) return;
                description.getSelected().edit(core);
            }
        });

        selectModels = new ArrayList<>();


        selectModels.add(new DateSelect());
        selectModels.add(new SubjectSelect());

        currentModel = DATE_MODEL;
        activateModel();

        container.setBackground(core.colors.getColor("back"));
    }

    private ArrayList<SelectModel> selectModels;
    private int currentModel;

    private static final int DATE_MODEL = 0;
    private static final int SUBJECT_MODEL = 1;

    private void nextModel() {
        currentModel = (currentModel + 1)  % selectModels.size();
        activateModel();
    }

    private void prevModel() {
        if (currentModel == 0)
            currentModel = selectModels.size() - 1;
        else
            currentModel -= 1;
        activateModel();
    }

    private void activateModel() {
        dateSelect = selectModels.get(currentModel);
        dateSelect.select();

        typeLB.setText(Locale.get(currentModel + "DM"));
    }

    private class DateSelect extends SelectModel {
        Label
            dayLB,
            monthLB,
            yearLB;

        @Override
        void update() {
            dayLB.setText(SimpleDate.tToStr(model.date.get(Calendar.DAY_OF_MONTH)));
            monthLB.setText(SimpleDate.tToStr(model.date.get(Calendar.MONTH)));
            yearLB.setText(SimpleDate.tToStr(model.date.get(Calendar.YEAR) % 100));
        }

        TextButton newButton(String text, int whatAdd, int amount) {
            TextButton button = TextButtonFactory.make("BIG", text);
            button.addListener(new AddListener(whatAdd, amount) {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    update();
                }
            });
            return button;
        }

        @Override
        void create() {
            dayLB = LabelFactory.make("BIG", "00");
            monthLB = LabelFactory.make("BIG", "00");
            yearLB = LabelFactory.make("BIG", "00");


            TextButton dayAdd, dayDis;
            dayAdd = newButton("+", Calendar.DAY_OF_MONTH, 1);
            dayDis = newButton("-", Calendar.DAY_OF_MONTH, -1);



            TextButton monthAdd = newButton("+", Calendar.MONTH, 1);
            TextButton monthDis = newButton("-", Calendar.MONTH, -1);

            TextButton yearAdd = newButton("+", Calendar.YEAR, 1);
            TextButton yearDis = newButton("-", Calendar.YEAR, -1);

            beginUI()
                .r(2.5f)
                .r(30f).c(0.1f).c(0.2f, dayDis).c(0.1f).c(0.2f, monthDis).c(0.1f).c(0.2f, yearDis).c(0.1f)


                .r(2.5f)
                .r(30f).c(0.1f).c(0.2f, dayLB).c(0.1f, point()).c(0.2f, monthLB).c(0.1f, point()).c(0.2f, yearLB).c(0.1f)
                .r(2.5f)

                .r(30f).c(0.1f).c(0.2f, dayAdd).c(0.1f).c(0.2f, monthAdd).c(0.1f).c(0.2f, yearAdd).c(0.1f)
                .r(2.5f)
            ;
            endUI();
        }
        private Label point() {
            return LabelFactory.make("BIG_NO_B", ".");
        }
    }
    private class AddListener extends TextButtonFactory.UpListener {
        int whatAdd;
        int amount;



        AddListener(int whatAdd, int amount) {
            this.whatAdd = whatAdd;
            this.amount = amount;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            model.date.add(whatAdd, amount);
        }
    }

    private class SubjectSelect extends SelectModel {
        private int prevSub = -1;
        private int prevShift = -1;
        int subjectID = -1;

        int shift = 0;
        Label name, teacher;
        Label pdate, lessonName;

        NextLessonInfo info = new NextLessonInfo();


        private void dateUpdated() {
            prevSub = subjectID;
            prevShift = shift;

            model.date = info.getByShift(shift);

            if (model.date != null) {
                pdate.setText(
                          SimpleDate.tToStr(model.date.get(Calendar.DAY_OF_MONTH)) + "."
                        + SimpleDate.tToStr(model.date.get(Calendar.MONTH)) + "."
                        + SimpleDate.tToStr(model.date.get(Calendar.YEAR) % 100)
                );
            } else {
                model.date = DataAPI.cloneSelectedDate();
            }

        }
        @Override
        void update() {
            if (subjectID == -1) {

                name.setText("Предмет не выбран");
                teacher.setText("");
                pdate.setText("");
                lessonName.setText("<Никогда>");
            } else {
                Entry subject = DataAPI.object.get(subjectID);
                name.setText((String)subject.get("name"));
                teacher.setText((String)subject.get("teacher"));


                switch (shift) {
                    case -1:
                        lessonName.setText("Предыд. пара");
                        break;
                    case 0:
                        lessonName.setText("Текущая пара");
                        break;
                    case 1:
                        lessonName.setText("След. пара");
                        break;
                    case 2:
                        lessonName.setText("Через 1 пару");
                        break;
                    case 3:
                    case 4:
                        lessonName.setText("Через " + (shift - 1) + " пары");
                        break;

                    case -2:
                        lessonName.setText("1 пару назад");
                        break;
                    case -3:
                    case -4:
                        lessonName.setText(Math.abs(shift + 1) + " пары назад");
                        break;
                    default:
                        if (shift > 0)
                            lessonName.setText("Через " + (shift - 1) + " пар");
                        else
                            lessonName.setText(Math.abs(shift + 1) + " пар назад");
                }

                if (prevSub != subjectID) {
                    info.init(subjectID);
                    info.alignFromDate(Core.getCurrentDate(), 20);
                    dateUpdated();
                } else if (prevShift != shift) {
                    dateUpdated();
                }



            }


        }


        private Label lab(String type) {
            return LabelFactory.make(type, "");
        }
        @Override
        void create() {
            AlignedButton button = TextButtonFactory.make("BIG", null, new AlignedGroup(
                    new GroupRow(3f, 1f, 8f, 1f),
                    new GroupRow(1f, 1f, 8f, 1f),
                    new GroupRow(2f, 1f, 8f, 1f)
            ), true);
            button.addListener(new TextButtonFactory.UpListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    VariableSystem localVars = core.varsOf("subSelect");
                    localVars.setFlag("isSelect");
                    core.setScreen("subSelect");
                }
            });

            name = lab("MEDIUM_NO_B");
            teacher = lab("INFO_NO_B");
            button.setActor(teacher, 0, 1);
            button.setActor(name, 2, 1);

            lessonName = lab("MEDIUM");
            pdate = lab("INFO");

            TextButton left, right;
            left = TextButtonFactory.make("BIG", "<");
            left.addListener(new TextButtonFactory.UpListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    shift -= 1;
                    update();
                }
            });
            right = TextButtonFactory.make("BIG", ">");
            right.addListener(new TextButtonFactory.UpListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    shift += 1;
                    update();
                }
            });

            beginUI()
                    .r(5f)

                    .r(20f).c(0.05f).c(0.2f, left).c(0.05f).c(0.4f, pdate) .c(0.05f).c(0.2f, right).c(0.05f)
                    .r(20f).c(0.05f).c(0.2f, left).c(0.05f).c(0.4f, lessonName)      .c(0.05f).c(0.2f, right).c(0.05f)

                    .r(10f)

                    .r(40f).c(0.05f).c(0.9f, button).c(0.05f)

                    .r(5f)
            ;
            endUI();
        }
    }

    private abstract class SelectModel {
        private AlignedGroup group;
        private AlignedGroupConstructor constructor;


        SelectModel() {
            create();
        }

        abstract void update();
        abstract void create();


        private void select() {
            container.setActor(group);
            update();
        }

        AlignedGroupConstructor beginUI() {
            constructor = new AlignedGroupConstructor();
            return constructor;
        }

        void endUI() {
            group = constructor.getGroup();
            constructor = null;
        }

    }



    private static class DescriptionFactory extends AbstractLocalFactory<VisibleObject, NewTaskScreen> {
        DescriptionFactory(NewTaskScreen newTaskScreen) {
            super(newTaskScreen);
        }

        @Override
        protected Button newButton(VisibleObject data) {
            return data.getButton();
        }
    }
}
