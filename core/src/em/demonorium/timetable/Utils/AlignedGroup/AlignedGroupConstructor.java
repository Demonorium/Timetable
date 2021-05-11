package em.demonorium.timetable.Utils.AlignedGroup;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class AlignedGroupConstructor {
    private PriorityHandler<Actor, ActorInfo> container = new PriorityHandler<>();
    private ArrayList<GroupRow> rows = new ArrayList<>();

    private RowConstructor constructor = null;


    public static class ActorInfo {
        int x1, y1, x2, y2;

        public ActorInfo(int x1, int y1) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x1;
            this.y2 = y1;
        }

        public void extend(int x, int y) {
            int minX = Math.min(x, Math.min(x1, x2));
            int maxX = Math.max(x, Math.max(x1, x2));

            int minY = Math.min(y, Math.min(y1, y2));
            int maxY = Math.max(y, Math.max(y1, y2));

            this.x1 = minX;
            this.x2 = maxX;

            this.y1 = minY;
            this.y2 = maxY;
        }

    }

    public RowConstructor row(float sz) {
        return new RowConstructor(this, sz);
    }

    public RowConstructor r(float sz) {
        return row(sz);
    }


    public AlignedGroup getGroup() {
        if (constructor != null) {
            RowConstructor cnt = constructor;
            constructor = null;
            return cnt.getGroup();
        }
        GroupRow[] _rows = new GroupRow[rows.size()];
        for (int i = 0; i < _rows.length; ++i) {
            _rows[i] = rows.get(i);
        }
        AlignedGroup group = new AlignedGroup(_rows);

        container.begin();
        Map.Entry<Actor, ActorInfo> inf;
        while (container.hasNext()) {
            inf = container.next();
            group.setActor(inf.getKey(),
                    inf.getValue().x1, inf.getValue().y1,
                    inf.getValue().x2, inf.getValue().y2);
        }

        return group;
    }

    public static class RowConstructor {
        AlignedGroupConstructor parent;
        ArrayList<ActorSize> row = new ArrayList<>();
        float size;


        private void parentAddRow() {
            parent.constructor = null;
            if (row.isEmpty())
                parent.rows.add(new GroupRow(size, 1f));
            else {
                ActorSize[] sizes = new ActorSize[row.size()];
                for (int i = 0; i < sizes.length; ++i) {
                    sizes[i] = row.get(i);
                }
                parent.rows.add(new GroupRow(size, sizes));
            }

        }
        public RowConstructor(AlignedGroupConstructor parent, float size) {
            this.parent = parent;
            this.size = size;
            parent.constructor = this;
        }

        public RowConstructor row(float sz) {
            parentAddRow();
            return parent.row(sz);
        }

        public RowConstructor r(float sz) {
            return row(sz);
        }

        public RowConstructor cell(float size, Actor actor, int id) {
            if (actor != null) {
                ActorInfo info = parent.container.addEntry(actor, new ActorInfo(parent.rows.size(), row.size()), id);
                if (info != null)
                    info.extend(parent.rows.size(), row.size());
            }
            row.add(new ActorSize(size));
            return this;
        }

        public RowConstructor cell(float size, Actor actor) {
            return cell(size, actor, parent.container.getDefaultID());
        }

        public RowConstructor cell(float size) {
            return cell(size, null);
        }

        public RowConstructor cell(Actor actor) {
            return cell(1f, actor);
        }
        public RowConstructor cell(Actor actor, int id) {
            return cell(1f, actor, id);
        }


        public RowConstructor c(float size, Actor actor, int id) {
            return cell(size, actor, id);
        }

        public RowConstructor c(float size, Actor actor) {
            return cell(size, actor);
        }

        public RowConstructor c(float size) {
            return cell(size);
        }

        public RowConstructor c(Actor actor) {
            return cell( actor);
        }
        public RowConstructor c(Actor actor, int id) {
            return cell( actor, id);
        }

        public AlignedGroup getGroup() {
            parentAddRow();
            return parent.getGroup();
        }
    }
}
