package em.demonorium.timetable.Factory;

import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.Align;

import em.demonorium.timetable.Core;

public class ContainerFactory {
    final Core core;

    public ContainerFactory(Core core) {
        this.core = core;
    }

    public <T extends Actor> Container<T> get(T actor) {
        return new Container<T>(actor);
    }
    public <T extends Actor> Container<T> get(T actor, String back) {
        if (back == null)
            return get(actor);
        Container<T> container = get(actor);
        container.setBackground(core.colors.getColor(back));

        return container;
    }

    public <T extends Actor> Container<T> get(T actor, String back, int align) {
        if (back == null)
            return get(actor);
        Container<T> container = get(actor);
        container.align(align);
        container.setBackground(core.colors.getColor(back));

        return container;
    }

    public <T extends Actor> Container<T> get(T actor, int align) {
        Container<T> container = get(actor);
        container.align(align);
        return container;
    }


    public static class BoxContainer<T extends Actor> extends Container<Container<T>>{
        public BoxContainer() {
            super();
        }


        public BoxContainer(Container<T> actor) {
            super(actor);
        }

        public BoxContainer(T actor) {
            super(new Container<T>(actor));
        }


        public void setInBackground(Drawable drawable) {
            if (getActor() == null) return;
            getActor().setBackground(drawable);
        }

        public T getBasicActor() {
            if (getActor() == null) return null;
            return getActor().getActor();
        }

        public void setInPad(float pad) {
            if (getActor() == null) return;
            getActor().pad(pad);
        }

        @Override
        public void layout() {
            if (getActor() == null) return;
            float size = Math.min(getHeight(), getWidth());

            getActor().minSize(size);
            getActor().maxSize(size);
            super.layout();
        }
    }

    public <T extends Actor> BoxContainer<T> getBox(T actor) {
        return new BoxContainer<>(actor);
    }

    public <T extends Actor> BoxContainer<T> getBox(T actor, int align) {
        BoxContainer<T> container = getBox(actor);
        container.align(align);
        return container;
    }


    public <T extends Actor> BoxContainer<T> getBox(T actor, String drawable) {
        BoxContainer<T> container = getBox(actor);
        if (drawable != null)
            container.setBackground(core.colors.getColor(drawable));

        return container;
    }

    public <T extends Actor> BoxContainer<T> getBox(T actor, String drawable, String in) {
        BoxContainer<T> container = getBox(actor);
        if (drawable != null)
            container.setBackground(core.colors.getColor(drawable));
        if (in != null)
            container.setInBackground(core.colors.getColor(in));
        return container;
    }

    public <T extends Actor> BoxContainer<T> getBox(T actor, String drawable, int align) {
        BoxContainer<T> container = getBox(actor);
        container.align(align);
        if (drawable != null)
            container.setBackground(core.colors.getColor(drawable));

        return container;
    }

    public <T extends Actor> BoxContainer<T> getBox(T actor, String drawable, String in, int align) {
        BoxContainer<T> container = getBox(actor);
        container.align(align);
        if (drawable != null)
            container.setBackground(core.colors.getColor(drawable));
        if (in != null)
            container.setInBackground(core.colors.getColor(in));
        return container;
    }
}
