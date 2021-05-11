package em.demonorium.timetable.Menu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import em.demonorium.timetable.BasicScreen;
import em.demonorium.timetable.Core;
import em.demonorium.timetable.Factory.LabelFactory;
import em.demonorium.timetable.Factory.TextButtonFactory;
import em.demonorium.timetable.TimeData.Models.LessonModel;
import em.demonorium.timetable.Utils.AlignedGroup.AlignedGroup;
import em.demonorium.timetable.Utils.AlignedGroup.GroupRow;
import em.demonorium.timetable.Utils.Input.KeyboardInputAdapter;
import em.demonorium.timetable.Utils.SharedCollection.SharedCollection;

public class LessonEditScreen extends BasicScreen {
    private KeyboardInputAdapter auditory;
    private KeyboardInputAdapter building;


    private SharedCollection<Integer, LessonModel> lessons;
    private int lesson;

    private LessonModel getLesson() {
        return lessons.get(lesson);
    }


    public LessonEditScreen(Core _core) {
        super(_core, _core.colors.getBasicColor("base"));
    }


    @Override
    public void create() {

        UI = new AlignedGroup(
                new GroupRow(1f, 1f),
                new GroupRow(8f, 0.1f, 0.35f, 0.1f, 0.35f, 0.1f),
                new GroupRow(1f, 1f),

                new GroupRow(2f, 1f),

                new GroupRow(10f, 0.1f, 0.233f, 0.05f, 0.233f, 0.05f, 0.233f, 0.1f),
                new GroupRow(1f, 1f),
                new GroupRow(10f, 0.1f, 0.233f, 0.05f, 0.233f, 0.05f, 0.233f, 0.1f),

                new GroupRow(2f, 1f),

                new GroupRow(10f, 0.25f, 0.5f, 0.25f),
                new GroupRow(1f, 1f),
                new GroupRow(15f, 0.1f, 0.8f, 0.1f),

                new GroupRow(2f, 1f),

                new GroupRow(10f, 0.25f, 0.5f, 0.25f),
                new GroupRow(1f, 1f),
                new GroupRow(15f, 0.1f, 0.8f, 0.1f),

                //new GroupRow(40f, 0.1f, 0.8f, 0.1f),

                new GroupRow(10f, 1f)
        );


        {
            TextButton cns = TextButtonFactory.make("MEDIUM", "Отмена");
            cns.addListener(new TextButtonFactory.UpListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    core.back();
                }
            });
            UI.setActor(cns, 1, 1);

            TextButton acp = TextButtonFactory.make("MEDIUM", "Подтвердить");
            acp.addListener(new TextButtonFactory.UpListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    if (vars.testAndSet("isLessonEdit", false)) {
                        LessonModel _lesson = getLesson();
                        _lesson.auditory = (auditory.getText());
                        _lesson.building = (building.getText());
                        lessons.updated(lesson);
                    } else {
                        int id = lessons.size() - 1;
                        while ((id >= 0) && lessons.get(id).isSpace())
                            --id;
                        ++id;
                        if ((id < lessons.size()) && lessons.get(id).isSpace()) {
                            LessonModel lesson = lessons.get(id);
                            lesson.subject = vars.getValue("selectedSubject");
                            lesson.auditory = auditory.getText();
                            lesson.building = building.getText();

                            lessons.update(id);
                        }
                        else {
                            lessons.add(lessons.size(), new LessonModel(
                                    (int) vars.getValue("selectedSubject"),
                                    auditory.getText(),
                                    building.getText()
                            ));
                        }
                    }

                    core.back();
                }
            });
            UI.setActor(acp, 1, 3);
        }

        building = core.inputFactory.get("main", "");
        Label teacherL = LabelFactory.make("BIG_NO_B", "Здание");

        auditory = core.inputFactory.get("main", "");
        Label nameL = LabelFactory.make("BIG_NO_B", "Аудитория");

        UI.setActor(nameL, 6 + 4 * 2, 1);
        UI.setActor(auditory.getActor(), 4 + 4 * 2, 1);

        UI.setActor(teacherL, 6 + 4, 1);
        UI.setActor(building.getActor(), 4 + 4, 1);
    }

    @Override
    public void show() {
        super.show();
        lessons = vars.getValue("lessonList");
        if (vars.testFlag("isLessonEdit")) {
            lesson = vars.getValue("editLesson");
            auditory.setText(getLesson().auditory);
            building.setText(getLesson().building);
        }
    }
}
