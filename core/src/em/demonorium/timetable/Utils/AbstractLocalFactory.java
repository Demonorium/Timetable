package em.demonorium.timetable.Utils;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;

import java.util.ArrayList;
import java.util.Collections;

import em.demonorium.timetable.Factory.TextButtonFactory;

public abstract class AbstractLocalFactory<T, source> implements ButtonList.LocalButtonFactory<T> {
    final private ArrayList<Disableable> onSelected = new ArrayList<>();
    final private ArrayList<Disableable> onUnselected = new ArrayList<>();


    final protected source Source;



    protected ButtonList<T> group;

    protected abstract Button newButton(T data);

    protected void onClick() {}
    protected void onSelected() {}
    protected void onUnselected() {}


    private static void setDisabled(ArrayList<Disableable> disableables, boolean status) {
        for (Disableable disableable: disableables)
            disableable.setDisabled(status);
    }

    @Override
    public Button get(T data) {
        Button button = newButton(data);
        button.addListener(new TextButtonFactory.UpListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                boolean hasSelected = group.hasSelected();
                setDisabled(onSelected, !hasSelected);
                setDisabled(onUnselected, hasSelected);
                onClick();
                if (hasSelected)
                    onSelected();
                else
                    onUnselected();
            }
        });
        return button;
    }

    public void addActors(boolean on, Disableable ... actors) {
        if (on) {
            Collections.addAll(onSelected, actors);
        } else {
            Collections.addAll(onUnselected, actors);
        }
    }

    public void addActors( Disableable ... actors) {
        addActors(true, actors);
    }

    public AbstractLocalFactory(source source) {
        Source = source;
    }

    public void update() {
        boolean hasSelected = group.hasSelected();
        setDisabled(onSelected, !hasSelected);
        setDisabled(onUnselected, hasSelected);
    }

    @Override
    public void init(ButtonList<T> group) {
        this.group = group;
    }
}
