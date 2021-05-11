package em.demonorium.timetable.Menu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import em.demonorium.timetable.BasicScreen;
import em.demonorium.timetable.Core;
import em.demonorium.timetable.Factory.ContainerFactory;
import em.demonorium.timetable.Factory.LabelFactory;
import em.demonorium.timetable.Factory.TextButtonFactory;
import em.demonorium.timetable.TimeData.DataAPI;
import em.demonorium.timetable.TimeData.DataBase.Entry;
import em.demonorium.timetable.Utils.AlignedGroup.AlignedGroup;
import em.demonorium.timetable.Utils.AlignedGroup.GroupRow;
import em.demonorium.timetable.Utils.Input.KeyboardInputAdapter;
import em.demonorium.timetable.Utils.SharedCollection.SharedCollection;

public class SubjectEditScreen extends BasicScreen {


    public SubjectEditScreen(Core _core) {
        super(_core,  _core.colors.getBasicColor("base"));
    }

    private KeyboardInputAdapter name;
    private KeyboardInputAdapter teacher;

    private ButtonGroup group;
    Button left, center, right;

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
                    String type;
                    if (group.getCheckedIndex() == 0) {
                        type = "none";
                    } else if (group.getCheckedIndex() == 1) {
                        type = "test";
                    } else
                        type = "exam";

                    if (vars.testFlag("edit")) {
                        Entry entry = vars.getValue("subject");
                        entry.set("name", name.getText());
                        entry.set("teacher", teacher.getText());
                        entry.set("exam", type);

                        ((SharedCollection<Integer, Entry>)DataAPI.object.get("subjects")).updateByValue(entry);
                    } else
                        DataAPI.subjects.add(name.getText(), teacher.getText(), type);

                    core.back();
                }
            });
            UI.setActor(acp, 1, 3);
        }

        group = new ButtonGroup();

        {

            ContainerFactory.BoxContainer<Button> cnt = core.makeFlag("FLAG");
            left = cnt.getBasicActor();
            UI.setActor(cnt, 4, 1);

            cnt = core.makeFlag("FLAG");
            center = cnt.getBasicActor();
            UI.setActor(cnt, 4, 3);

            cnt = core.makeFlag("FLAG");
            right = cnt.getBasicActor();
            UI.setActor(cnt, 4, 5);

            group.add(left, center, right);
        }
        UI.setActor(LabelFactory.make("MEDIUM_NO_B", "Ничего"), 6, 1);
        UI.setActor(LabelFactory.make("MEDIUM_NO_B", "Зачёт"),        6, 3);
        UI.setActor(LabelFactory.make("MEDIUM_NO_B", "Экзамен"),      6, 5);

        teacher = core.inputFactory.get("main", "Преподаватель");
        Label teacherL = LabelFactory.make("BIG_NO_B", "Имя преподавателя");

        name = core.inputFactory.get("main", "Предмет");
        Label nameL = LabelFactory.make("BIG_NO_B", "Название предмета");

        UI.setActor(nameL, 6 + 4*2, 1);
        UI.setActor(name.getActor(), 4 + 4*2, 1);

        UI.setActor(teacherL, 6 + 4, 1);
        UI.setActor(teacher.getActor(), 4 + 4, 1);

    }

    @Override
    public void show() {
        super.show();
        if (vars.testFlag("edit")) {
            Entry subject = vars.getValue("subject");
            name.setText((String) subject.get("name"));
            teacher.setText((String) subject.get("teacher"));
            String type = subject.get("exam");
            if (type.equals("none"))
                left.setChecked(true);
            else if (type.equals("test"))
                center.setChecked(true);
            else if (type.equals("exam"))
                right.setChecked(true);
        } else {
            name.setText("Предмет");
        }
    }
}
