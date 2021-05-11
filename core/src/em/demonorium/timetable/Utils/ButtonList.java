package em.demonorium.timetable.Utils;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import em.demonorium.timetable.Core;
import em.demonorium.timetable.Utils.SharedCollection.Editable;
import em.demonorium.timetable.Utils.SharedCollection.SharedCollection;
import em.demonorium.timetable.Utils.SharedCollection.SimpleCollection;
import em.demonorium.timetable.Utils.SharedCollection.SimpleIterator;

public class ButtonList<T> extends SharedCollection<Integer, T> {
    private final VerticalGroup   list;
    private final ButtonGroup<Button> group;
    private final LocalButtonFactory<T>     factory;

    private SharedCollection<Integer, T> content;



    public ButtonList(SharedCollection<Integer, T> content, LocalButtonFactory<T> factory) {
        super(content);

        this.content = content;
        this.factory = factory;

        this.list = new VerticalGroup();
        this.group = new ButtonGroup<>();
        group.setMinCheckCount(0);
        list.grow();
        list.space(5f);

        factory.init(this);
        content.register(this);

        for (SimpleIterator<Integer, T> iterator = content.iterator(); iterator.hasNext(); ) {
            iterator.next();
            added(iterator.key(), iterator.get());
        }
    }

    public void select(int id) {
        ((Button)list.getChild(id)).setChecked(true);
    }

    public void setContent(SharedCollection<Integer, T> content) {
        if (this.content != null) {
            cleared();
            content.removeRegistration(this);
        }
        this.content = content;
        content.register(this);

        for (SimpleIterator<Integer, T> iterator = content.iterator(); iterator.hasNext(); ) {
            iterator.next();
            added(iterator.key(), iterator.get());
        }
    }

    public boolean hasSelected() {
        return group.getCheckedIndex() != -1;
    }

    public VerticalGroup getVL() {
        return list;
    }

    public ButtonGroup<Button> getGroup() {
        return group;
    }

    public void add(T value) {
        add(size(), value);
    }

    public T getSelected() {
        if (hasSelected())
            return content.get(getSelectedIndex());
        return null;
    }

    public Integer getSelectedIndex() {
        return list.getChildren().indexOf(group.getChecked(), true);
    }

    public void remove() {
        remove(getSelectedIndex());
    }

    @Override
    public void added(Integer aInteger, T data) {
        Button button = factory.get(data);

        list.addActorAt(aInteger, button);
        group.add(button);
    }

    @Override
    public void removed(Integer aInteger) {
        Button button = (Button) list.getChild(aInteger);
        list.removeActor(button);
        group.remove(button);
    }

    @Override
    public void cleared() {
        group.clear();
        list.clear();
    }

    @Override
    public void updated(Integer integer) {
        removed(integer);
        added(integer, content.get(integer));
    }

    public void select(Integer integer) {
        ((Button)list.getChild(integer)).setChecked(true);
    }

    @Override
    public SimpleIterator<Integer, T> iterator() {
        return content.iterator();
    }

    public interface LocalButtonFactory<T> {
        Button get(T data);

        void init(ButtonList<T> group);
    }

    public void update() {
        cleared();
        for (SimpleIterator<Integer, T> iterator = content.iterator(); iterator.hasNext(); ) {
            iterator.next();
            added(iterator.key(), iterator.get());
        }
    }

    @Override
    public boolean isEmpty() {
        return content.isEmpty();
    }

    @Override
    public boolean containsKey(Integer key) {
        return content.containsKey(key);
    }

    @Override
    public boolean containsValue(T value) {
        return content.containsValue(value);
    }

    @Override
    public Integer keyOf(T t) {
        return content.keyOf(t);
    }
}
