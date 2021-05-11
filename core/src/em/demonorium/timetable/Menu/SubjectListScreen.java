package em.demonorium.timetable.Menu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;


import em.demonorium.timetable.BasicScreen;
import em.demonorium.timetable.Core;
import em.demonorium.timetable.Factory.LabelFactory;
import em.demonorium.timetable.Factory.ScrollPaneFactory;
import em.demonorium.timetable.Factory.TextButtonFactory;
import em.demonorium.timetable.TimeData.DataAPI;
import em.demonorium.timetable.TimeData.DataBase.Entry;
import em.demonorium.timetable.Utils.AbstractLocalFactory;
import em.demonorium.timetable.Utils.AlignedButton;
import em.demonorium.timetable.Utils.AlignedGroup.AlignedGroup;
import em.demonorium.timetable.Utils.AlignedGroup.GroupRow;
import em.demonorium.timetable.Utils.ButtonList;
import em.demonorium.timetable.Utils.SharedCollection.Default.SharedList;
import em.demonorium.timetable.Utils.VariableSystem.Action;
import em.demonorium.timetable.Utils.VariableSystem.VariableSystem;

public class SubjectListScreen extends BasicScreen {

    protected ButtonList<Entry> list;
    private TextButton del, backBT, edit;

    private SubjectButtonFactory factory;
    private Label title;

    public SubjectListScreen(Core _core) {
        super(_core,  _core.colors.getBasicColor("base"));


        del = TextButtonFactory.make("BIG", "-");
        del.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (del.isDisabled()) return;

                //"listYN", new IfScreen(this, "S_LYN_what", "S_LYN_yes", "S_LYN_no"
                if (list.hasSelected()) {
                    VariableSystem localVars = core.varsOf("listYN");
                    localVars.set("what", "Удалить?");
                    localVars.set("yes", new Action() {
                        @Override
                        public void action() {
                            DataAPI.base().destroy(list.getSelected().ID);
                            list.remove();
                        }
                    });
                    localVars.set("no", Action.NONE_ACTION);

                    core.setScreen("listYN");
                }
            }
        });
        UI.setActor(del, 4, 5);

        edit = TextButtonFactory.make("BIG", "E");
        edit.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (list.hasSelected()) {
                    VariableSystem local = core.varsOf("newSubject");
                    local.setFlag("edit", true);
                    local.set("subject", list.getSelected());

                    core.setScreen("newSubject");
                }
            }
        });
        UI.setActor(edit, 4, 3);


        backBT = TextButtonFactory.make("BIG", "Назад");
        backBT.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (!vars.testFlag("isSelect"))
                    core.back();

                if (vars.testAndSet("hasNext", false)) {
                    if (!list.hasSelected()) {
                        core.back();
                        return;
                    }
                    core.forceBack();
                    String id = vars.getValue("next");


                    core.varsOf(id).set("selectedSubject", list.getSelected().ID);

                    core.setScreen(id);
                } else {
                    if (list.hasSelected()) {
                        core.returnValues().setFlag("subjectSelected");
                        core.returnValues().set("selectedSubject", list.getSelected().ID);
                    } else {
                        core.returnValues().set("selectedSubject", -1);
                    }
                    core.back();
                }

            }
        });
        UI.setActor(backBT, 1, 1);

        factory = new SubjectButtonFactory(this);
        factory.addActors(edit);
        list = new ButtonList<>((SharedList<Entry>)DataAPI.base().get("subjects"), factory);
        UI.setActor(ScrollPaneFactory.make("main", list.getVL()), 6, 1);
    }

    @Override
    public void create() {
        UI = new AlignedGroup(
                new GroupRow(1f, 1f),
                new GroupRow(8f, 0.1f, 0.8f, 0.1f),
                new GroupRow(1f, 1f),

                new GroupRow(1f, 1f),
                new GroupRow(8f, 0.1f, 0.6f/3f, 0.1f, 0.6f/3f, 0.1f, 0.6f/3f, 0.1f),
                new GroupRow(1f, 1f),

                new GroupRow(80f, 0.1f, 0.8f, 0.1f),

                new GroupRow(10f, 1f)
        );

        title = LabelFactory.make("BIG_NO_B", "Выберите");
        UI.setActor(title,7, 0);


        TextButton edit = TextButtonFactory.make("BIG", "+");
        edit.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                VariableSystem local = core.varsOf("newSubject");
                local.setFlag("edit", false);
                core.setScreen("newSubject");
            }
        });
        UI.setActor(edit, 4, 1);

    }

    @Override
    public void show() {
        super.show();
        del.setDisabled(!list.hasSelected());
        edit.setDisabled(!list.hasSelected());

        if (vars.testFlag("isSelect"))
            title.setText("Выберите");
        else
            title.setText("Предметы");

        updateButton();
        factory.update();
    }


    private void updateButton() {
        if (vars.testFlag("isSelect")) {
            if (list.hasSelected()) {
                backBT.setText("Выбрать");
            } else {
                backBT.setText("Назад");
            }
        } else {
            backBT.setText("Назад");
            del.setDisabled(!list.hasSelected());
        }

    }

    public static class SubjectButtonFactory extends AbstractLocalFactory<Entry, SubjectListScreen> {

        public SubjectButtonFactory(SubjectListScreen subjectListScreen) {
            super(subjectListScreen);
        }

        @Override
        protected void onSelected() {
            Source.updateButton();
            super.onSelected();
        }

        @Override
        protected Button newButton(Entry data) {
            AlignedButton button = TextButtonFactory.make("MEDIUM", Core.fontSettings.main.regular.getCapHeight()*2.3f, new AlignedGroup(
                    new GroupRow(1f, 1f),

                    new GroupRow(10f,   0.05f, 0.6f, 0.05f, 0.25f, 0.05f),
                    new GroupRow(1f,    0.05f),
                    new GroupRow(4f,   0.05f, 0.6f, 0.05f, 0.25f, 0.05f),

                    new GroupRow(1f, 1f)
            ));

            Label label = LabelFactory.make("MEDIUM_NO_B", (String)data.get("name"));
            label.setAlignment(Align.left);
            button.setActor(label, 3, 1);
            label = LabelFactory.make("INFO_NO_B", (String)data.get("teacher"));
            label.setAlignment(Align.left);
            button.setActor(label, 1, 1);

            if (data.get("exam").equals("exam")) {
                button.setActor(LabelFactory.make("MEDIUM_NO_B", "ЭКЗ"), 1, 3, 3, 3);
            } else if (data.get("exam").equals("test")) {
                button.setActor(LabelFactory.make("MEDIUM_NO_B", "ЗАЧ"), 1, 3, 3, 3);
            }

            return button;
        }
    }
}
