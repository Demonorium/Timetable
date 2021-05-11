package em.demonorium.timetable.Menu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import java.util.ArrayList;

import em.demonorium.timetable.BasicScreen;
import em.demonorium.timetable.Core;
import em.demonorium.timetable.Factory.LabelFactory;
import em.demonorium.timetable.Factory.TextButtonFactory;
import em.demonorium.timetable.TimeData.Models.VisibleObjects.VisibleText;
import em.demonorium.timetable.TimeData.VisibleObject;
import em.demonorium.timetable.Utils.AlignedButton;
import em.demonorium.timetable.Utils.AlignedGroup.AlignedGroup;
import em.demonorium.timetable.Utils.AlignedGroup.AlignedGroupConstructor;
import em.demonorium.timetable.Utils.Input.KeyboardInputAdapter;
import em.demonorium.timetable.Utils.VariableSystem.VariableSystem;

public class TaskElementEditor extends BasicScreen {

    private Label typeLB;

    public TaskElementEditor(Core _core) {
        super(_core, _core.colors.getBasicColor("base"));
    }


    private Container<AlignedGroup> container;
    private ContainerClass currentContainer;
    private ArrayList<ContainerClass> containers;
    private int currentID;


    private void select(int container) {
        currentID = container;
        currentContainer = containers.get(currentID);
        currentContainer.select();
    }
    private void nextContainer() {
        select((currentID + 1) % containers.size());
    }

    private void prevContainer() {
        if (currentID == 0)
            select(containers.size() - 1);
        else
            select(currentID - 1);
    }

    @Override
    public void show() {
        super.show();
        for (ContainerClass container: this.containers)
            container.clear();
    }



    @Override
    public void create() {
        container = new Container<>();
        container.background(core.colors.getColor("back"));
        container.fill();
        TextButton backBT = TextButtonFactory.make("MEDIUM", "Назад");
        backBT.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                core.back();
            }
        });

        TextButton acceptBT = TextButtonFactory.make("MEDIUM", "Добавить");
        acceptBT.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                VariableSystem vars = core.returnValues();
                vars.setFlag("returnVisible");
                vars.set("visible", currentContainer.getObject());
                core.back();
            }
        });


        typeLB = LabelFactory.make("MEDIUM", "ТИП");

        TextButton leftBT = TextButtonFactory.make("BIG", "<");
        leftBT.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                prevContainer();
            }
        });
        TextButton rightBT = TextButtonFactory.make("BIG", ">");
        rightBT.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                nextContainer();
            }
        });

        containers = new ArrayList<>();
        containers.add(new ContainerText());
        select(0);

        beginUI()
                .r(1f)
                .r(8f).c(0.1f).c(0.35f, backBT).c(0.1f).c(0.35f, acceptBT).c(0.1f)
                .r(1f)

                .r(80f).c(container)

                .r(1f)
                .r(8f).c(0.1f).c(0.1f, leftBT).c(0.05f).c(0.5f, typeLB).c(0.05f).c(0.1f, rightBT).c(0.1f)
                .r(1f);
        endUI();
    }


    private class ContainerText extends ContainerClass{
        KeyboardInputAdapter input;
        public ContainerText() {
            super("Текст");
            input = core.inputFactory.get("main", "");
            Label label = LabelFactory.make("MEDIUM", "Отображаемый тест");
            beginUI()
                    .r(30f)
                    .r(10f).c(0.1f).c(0.8f, label).c(0.1f)
                    .r(20f).c(0.1f).c(0.8f, input.getActor()).c(0.1f)
                    .r(40f);
            endUI();
        }

        @Override
        void update() {




        }

        @Override
        void clear() {
            input.setText("");
        }


        @Override
        VisibleObject getObject() {
            return new VisibleText(input.getText());
        }
    }


    private abstract class ContainerClass {
        private final String name;
        private AlignedGroupConstructor constructor;
        AlignedGroup group;


        public ContainerClass(String name) {
            this.name = name;
        }

        abstract void update();
        abstract void clear();
        abstract VisibleObject getObject();

        void select() {
            container.setActor(group);
            typeLB.setText(name);
            update();
        }

        AlignedGroupConstructor beginUI() {
            return constructor = new AlignedGroupConstructor();
        }

        void endUI() {
            group = constructor.getGroup();
            constructor = null;
        }

    }

}
