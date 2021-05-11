package em.demonorium.timetable.Utils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;

public class FixedButton<T extends Actor> extends Button {
    final protected T actor;
    protected Container<T> container;

    private void init(Float w, Float h) {

        if (actor != null)
            container = new Container<T>(actor);
        else
            container = new Container<>();

        container.setClip(false);



        if ((w != null) && (h != null)) {
            container.minSize(w, h);
            container.maxSize(w, h);

            add(container).expand().fill();
            setSize(w, h);
            return;
        }

        if (w != null) {
            container.minWidth(w);
            container.maxWidth(w);

            add(container).expand().fill();
            setSize(w, getPrefHeight());
            return;
        }
        if (h != null) {
            container.minHeight(h);
            container.maxHeight(h);

            add(container).expand().fill();
            setSize(getPrefWidth(), h);
            return;
        }
        add(container).expand().fill();
        container.setFillParent(true);
    }

    public FixedButton(ButtonStyle style, T actor) {
        super(style);
        this.actor = actor;
        init(null, null);
    }

    public FixedButton(ButtonStyle style, T actor, Float w) {
        super(style);
        this.actor = actor;
        init(w, null);
    }

    public FixedButton(ButtonStyle style, T actor, Float w, Float h) {
        super(style);
        this.actor = actor;
        init(w, h);
    }
}
