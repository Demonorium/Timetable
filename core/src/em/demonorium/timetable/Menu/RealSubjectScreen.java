package em.demonorium.timetable.Menu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;

import java.util.Calendar;
import java.util.GregorianCalendar;

import em.demonorium.timetable.BasicScreen;
import em.demonorium.timetable.Core;
import em.demonorium.timetable.Factory.LabelFactory;
import em.demonorium.timetable.Factory.TextButtonFactory;
import em.demonorium.timetable.TimeData.DataAPI;
import em.demonorium.timetable.TimeData.DataBase.Entry;
import em.demonorium.timetable.TimeData.Models.DayModel;
import em.demonorium.timetable.TimeData.Models.LessonModel;
import em.demonorium.timetable.TimeData.Prototypes;
import em.demonorium.timetable.Utils.AlignedButton;
import em.demonorium.timetable.Utils.AlignedGroup.AlignedGroup;
import em.demonorium.timetable.Utils.AlignedGroup.GroupRow;
import em.demonorium.timetable.Utils.Input.KeyboardInputAdapter;
import em.demonorium.timetable.Utils.SharedCollection.Default.SharedList;
import em.demonorium.timetable.Utils.VariableSystem.VariableSystem;

public class RealSubjectScreen extends BasicScreen {

    private Label dateLB;
    private Label teacherTX;
    private Label subjectTX;

    public RealSubjectScreen(Core _core) {
        super(_core, _core.colors.getBasicColor("base"));
    }


    private TextButton newTask, accept, cancel;
    private KeyboardInputAdapter auditory, building;

    private Button nothingBT, examBT, testBT;
    private ButtonGroup<Button> subTypeGroup;



    private int subjectID;
    private GregorianCalendar date;
    private LessonModel lesson = new LessonModel();
    private int lessonID;

    private static String getDate(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR);
    }

    @Override
    public void show() {
        super.show();
        if (vars.testAndSet("loadLesson", false)) {
            lesson.fromEntry((Entry) vars.getValue("lesson"));
            lessonID = vars.getValue("lessonID");

            auditory.setText(lesson.auditory);
            building.setText(lesson.building);

            subjectID = lesson.subject;
            subjectTX.setText((String)DataAPI.object.get(subjectID).get("name"));
            teacherTX.setText((String)DataAPI.object.get(subjectID).get("teacher"));
        }
        if (vars.testAndSet("loadDate", false)) {
            date = vars.getValue("date");
            dateLB.setText(getDate(date));
        }
        if (vars.testAndSet("subjectSelected", false)) {
            subjectID = vars.getValue("selectedSubject");
            subjectTX.setText((String)DataAPI.object.get(subjectID).get("name"));
            teacherTX.setText((String)DataAPI.object.get(subjectID).get("teacher"));
        }
    }

    @Override
    public void create() {
        cancel = TextButtonFactory.make("MEDIUM", "Отмена");
        cancel.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                core.back();
            }
        });
        accept = TextButtonFactory.make("MEDIUM", "Подтвердить");
        accept.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                lesson.auditory = auditory.getText();
                lesson.building = building.getText();
                lesson.subject = subjectID;

                DayModel newDay = new DayModel();
                newDay.fromEntry(DataAPI.getDay(date));

                newDay.lessons.set(lessonID, lesson);

                DataAPI.addChanges(date, newDay);
                core.back();
            }
        });


        newTask = TextButtonFactory.make("BIG", "Добавить задание");
        newTask.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                VariableSystem localVars = core.varsOf("newTask");
                localVars.setFlag("loadSubject");
                localVars.set("subject", subjectID);

                localVars.setFlag("loadDate");
                localVars.set("date", date.clone());
                core.setScreen("newTask");
            }
        });


        Label subLB = LabelFactory.make("BIG_NO_B", "Предмет");
        AlignedButton subChangeButton = TextButtonFactory.make("MEDIUM", (Float)null, new AlignedGroup(
                new GroupRow(2f, 1f, 9f),
                new GroupRow(1f, 1f),
                new GroupRow(3f, 1f, 9f)
        ), true);

        teacherTX = LabelFactory.make("INFO_NO_B", "");
        subjectTX = LabelFactory.make("MEDIUM_NO_B", "");

        subjectTX.setAlignment(Align.left);
        teacherTX.setAlignment(Align.left);

        subChangeButton.setActor(teacherTX, 0, 1);
        subChangeButton.setActor(subjectTX, 2, 1);

        subChangeButton.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                VariableSystem localVars = core.varsOf("subSelect");
                localVars.setFlag("isSelect");
                core.setScreen("subSelect");
            }

        });



        dateLB = LabelFactory.make("BIG", "Дата");


        Label auditoryLB = LabelFactory.make("BIG_NO_B", "Аудитория:");
        auditoryLB.setAlignment(Align.right);
        Label buildingLB = LabelFactory.make("BIG_NO_B", "Здание:");
        buildingLB.setAlignment(Align.right);
        auditory = core.inputFactory.get("main", "");
        building = core.inputFactory.get("main", "");


        beginUI()
                //Кнопки назад и принять
                .r(1f)
                .r(8f).c(0.1f).c(0.35f, cancel).c(0.1f).c(0.35f, accept).c(0.1f)
                .r(1f)

                //Кнопка нового задания
                .r(6f)
                .r(8f).c(0.1f).c(0.8f, newTask).c(0.1f)
                .r(6f)
                //30



                //Кнопки изменения кабинета и здания
                .r(5f)
                .r(10f).c(10f).c(41f, buildingLB).c(5f).c(34f, building.getActor()).c(10f)
                .r(5f)
                .r(10f).c(10f).c(41f, auditoryLB).c(5f).c(34f, auditory.getActor()).c(10f)
                .r(5f)
                //65

                //Смена предмета
                .r(10f).c(0.1f).c(0.8f, subChangeButton).c(0.1f)
                .r(5f)
                .r(10f).c(subLB)

                //90

                //Дата сверху
                .r(1f)
                .r(8f).c(1f).c(8f, dateLB).c(1f)
                .r(1f);
        endUI();
    }
}
