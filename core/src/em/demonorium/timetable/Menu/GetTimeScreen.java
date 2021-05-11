package em.demonorium.timetable.Menu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import em.demonorium.timetable.BasicScreen;
import em.demonorium.timetable.Core;
import em.demonorium.timetable.Factory.LabelFactory;
import em.demonorium.timetable.Factory.TextButtonFactory;
import em.demonorium.timetable.TimeData.SimpleDate;
import em.demonorium.timetable.Utils.AlignedGroup.AlignedGroup;
import em.demonorium.timetable.Utils.AlignedGroup.GroupRow;

public class GetTimeScreen extends BasicScreen {
    private SimpleDate date;
    private Label hoursT, hours, minutesT, minutes;

    public GetTimeScreen(Core _core) {
        super(_core);
    }


    public void update() {
        if (date == null) return;

        hoursT.setText(Integer.toString(date.getHours() / 10));
        hours.setText(Integer.toString(date.getHours() % 10));
        minutesT.setText(Integer.toString(date.getMinutes() / 10));
        minutes.setText(Integer.toString(date.getMinutes() % 10));
    }
    @Override
    public void show() {
        super.show();

        if (vars.declared("editedDate"))
            setDate(new SimpleDate((SimpleDate)vars.getValue("editedDate")));
        else
            setDate(new SimpleDate());


        update();
    }

    public void setDate(SimpleDate date) {
        this.date = date;
        update();
    }

    @Override
    public void create() {


        float sp = 2f;
        float part = (80f -sp*6 - 5f) / 4f;

        float[] group = {
                10f, //0
                sp, part, sp, part, sp, //1 2 3 4 5
                part/2f, // 6
                sp, part, sp, part, sp, //7 8 9 10 11
                10f}; //12



        this.UI = new AlignedGroup(
                new GroupRow(30f,   1f),

                new GroupRow(2f,    10f, 80f, 10f),

                new GroupRow(10f,   10f, 5f, 30f, 10f, 30f, 5f, 10f),

                new GroupRow(2f,    10f, 80f, 10f ),
                new GroupRow(4f,    group ), // 4
                new GroupRow(4f,    group ), // 5
                new GroupRow(4f,    group ), // 6

                new GroupRow(2f,    10f, 80f, 10f ),

                new GroupRow(28f,   1f)
        );
        UI.setActor(new Image(core.colors.getColor("base")), 1,1, 7, 1);


        UI.setActor(LabelFactory.make("BIG_NO_B", ":"), 5, 6);

        {
            TextButton add = TextButtonFactory.make("BIG", "+");
            add.addListener(new EditListener(this) {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    edit.date.add(SimpleDate.HOUR*10);
                    edit.update();
                }
            });
            UI.setActor(add, 6, 2);

            TextButton sub = TextButtonFactory.make("BIG", "-");
            sub.addListener(new EditListener(this) {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    edit.date.add(-SimpleDate.HOUR*10);
                    edit.update();
                }
            });
            UI.setActor(sub, 4, 2);
        }

        {
            TextButton add = TextButtonFactory.make("BIG", "+");
            add.addListener(new EditListener(this) {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    edit.date.addHour();
                    edit.update();
                }
            });
            UI.setActor(add, 6, 4);

            TextButton sub = TextButtonFactory.make("BIG", "-");
            sub.addListener(new EditListener(this) {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    edit.date.subHour();
                    edit.update();
                }
            });
            UI.setActor(sub, 4, 4);
        }


        {
            TextButton add = TextButtonFactory.make("BIG", "+");
            add.addListener(new EditListener(this) {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    edit.date.add(10);
                    edit.update();
                }
            });
            UI.setActor(add, 6, 8);

            TextButton sub = TextButtonFactory.make("BIG", "-");
            sub.addListener(new EditListener(this) {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    edit.date.add(-10);
                    edit.update();
                }
            });
            UI.setActor(sub, 4, 8);
        }
        {
            TextButton add = TextButtonFactory.make("BIG", "+");
            add.addListener(new EditListener(this) {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    edit.date.addMinute();
                    edit.update();
                }
            });
            UI.setActor(add, 6, 10);

            TextButton sub = TextButtonFactory.make("BIG", "-");
            sub.addListener(new EditListener(this) {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    edit.date.subMinute();
                    edit.update();
                }
            });
            UI.setActor(sub, 4, 10);
        }


        {
            TextButton cancel = TextButtonFactory.make("MEDIUM", "Отмена");
            cancel.addListener(new TextButtonFactory.UpListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    core.back();
                }
            });
            UI.setActor(cancel, 2, 2);

            TextButton accept = TextButtonFactory.make("MEDIUM", "Подтвердить");
            accept.addListener(new TextButtonFactory.UpListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    core.returnValues().setFlag("returnTime");
                    core.returnValues().set("editedTime", new SimpleDate(date));
                    core.back();
                }
            });
            UI.setActor(accept, 2, 4);
        }

        hours = LabelFactory.make("BIG", "0");
        minutes = LabelFactory.make("BIG", "0");
        hoursT = LabelFactory.make("BIG", "0");
        minutesT = LabelFactory.make("BIG", "0");


        UI.setActor(hoursT,     5, 2);
        UI.setActor(hours,      5, 4);
        UI.setActor(minutesT,   5, 8);
        UI.setActor(minutes,    5, 10);

        update();
    }

    static abstract class EditListener extends TextButtonFactory.UpListener {
        final GetTimeScreen edit;

        EditListener(GetTimeScreen editSimpleTime) {
            edit = editSimpleTime;
        }
    }

}
