package em.demonorium.timetable.Utils;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import em.demonorium.timetable.Utils.AlignedGroup.AlignedGroup;

public class AlignedButton extends FixedButton<AlignedGroup> {


    public void setActor(Actor actor, int x, int y) {
        this.actor.setActor(actor, x, y);
    }
    public void setActor(Actor actor, int x, int y, int x2, int y2) {
        this.actor.setActor(actor, x, y, x2, y2);
    }


    @Override
    public void layout() {

        super.layout();

        actor.setBounds(actor.getParent().getX(), actor.getParent().getY(),
                actor.getParent().getWidth(), actor.getParent().getHeight());
    }

    public AlignedButton(ButtonStyle style, Float h, AlignedGroup group) {
        super(style, group, null, h);
    }
}
