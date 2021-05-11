package em.demonorium.timetable.Utils.AlignedGroup;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Align;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;


public class AlignedGroup extends Group {

    // ВАЖНО: нумерация строк снизу вверх!
    private GroupRow[] rows;
    private float totalWeight;
    private HashMap<Actor, ActorInfo> info = new HashMap<>();



    public AlignedGroup(GroupRow... rows) {
        this.rows = rows;
        calcWeight();
    }
    public AlignedGroup(int rowsCount, int columnsCount) {
        rows = new GroupRow[rowsCount];

        ActorSize[] array = new ActorSize[columnsCount];
        for (int i = 0; i < columnsCount; ++i) {
            array[i] = new ActorSize(1f);
        }
        for (int i = 0; i < rowsCount; ++i) {
            rows[i] = new GroupRow(new ActorSize(1f), array);
        }
        calcWeight();
    }

    public AlignedGroup(float x, float y, float width, float height, GroupRow... rows) {
        this.rows = rows;
        setBounds(x, y, width, height);
    }
    public AlignedGroup(float width, float height, GroupRow... rows) {
        this.rows = rows;
        calcWeight();
        setBounds(0, 0, width, height);
    }

    public AlignedGroup(float width, float height, int rowsCount, int columnsCount) {
        rows = new GroupRow[rowsCount];

        ActorSize[] array = new ActorSize[columnsCount];
        for (int i = 0; i < columnsCount; ++i) {
            array[i] = new ActorSize(1f);
        }
        for (int i = 0; i < rowsCount; ++i) {
            rows[i] = new GroupRow(new ActorSize(1f), array);
        }

        calcWeight();
        setBounds(0, 0, width, height);
    }

    private void calcWeight() {
        totalWeight = 0;
        for (GroupRow row: rows) {
            totalWeight += row.getHeightProportion().getWeight();
        }
    }
    @Override
    protected void positionChanged() {
        for (Actor a: getChildren()) {
            applyInfo(info.get(a));
        }
    }

    @Override
    protected void sizeChanged() {
        float currentY = 0;
        float partialSize = 0;
        float fixedSize = 0;

        for (GroupRow row: rows) {
            row.calcShifts(getWidth());

            row.getHeightProportion().calcSize(totalWeight, getHeight());
            partialSize += row.getHeightProportion().getCurrentSize();
            if (row.getHeightProportion().isMaxSize())
                fixedSize += row.getHeightProportion().getCurrentSize();
        }

        float mult = (getHeight() - fixedSize) / (partialSize - fixedSize);

        for (GroupRow row : rows) {
            row.position = currentY;
            if (row.getHeightProportion().isMaxSize()) {
                currentY += row.getHeightProportion().getCurrentSize();
            } else {
                currentY += row.getHeightProportion().getCurrentSize() * mult;
            }
        }

        for (Actor a: getChildren()) {
            applyInfo(info.get(a));
        }
    }

    public void setActor(Actor actor, int row, int column) {
        setActor(actor, row, column, row, column);
    }

    public void setActor(Actor actor, int firstRow, int firstColumn, int secondRow, int secondColumn) {
        applyInfo(registerActor(actor, firstRow, firstColumn, secondRow, secondColumn));
        addActor(actor);
    }

    //Класс описывает положение актёра в сетке
    private static class ActorInfo {
        Actor actor;
        int fromX, fromY, toX, toY;

        ActorInfo(Actor actor, int fromX, int fromY, int toX, int toY) {
            this.actor = actor;
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }

        void update(int fromX, int fromY, int toX, int toY) {
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }
    }

    //Регистрация актёра в списке
    private ActorInfo registerActor(Actor actor, int fromX, int fromY, int toX, int toY) {
        if (info.containsKey(actor)) {
            ActorInfo i = info.get(actor);
            i.update(fromX, fromY, toX, toY);
            return i;
        }
        ActorInfo i = new ActorInfo(actor, fromX, fromY, toX, toY);
        info.put(actor, i);
        return i;
    }

    private float getXLeft(int row, int col) {
        return rows[row].getLeftX(col);
    }
    private float getXRight(int row, int col) {
        return rows[row].getRightX(col);
    }

    private float getYLeft(int row) {
        return rows[row].position;
    }
    private float getYRight(int row) {
        if ((row+1) < rows.length)
            return rows[row + 1].position;
        return getHeight();
    }

    private void applyInfo(ActorInfo info) {
        float T_X11 = getXLeft(info.fromX, info.fromY);
        float T_X21 = getXRight(info.fromX, info.fromY);
        float T_X12 = getXLeft(info.toX, info.toY);
        float T_X22 = getXRight(info.toX, info.toY);

        float X1 = Math.min(Math.min(T_X11, T_X22), Math.min(T_X21, T_X12));
        float X2 = Math.max(Math.max(T_X11, T_X22), Math.max(T_X21, T_X12));

        float T_Y11 = getYLeft(info.fromX);
        float T_Y21 = getYRight(info.fromX);
        float T_Y12 = getYLeft(info.toX);
        float T_Y22 = getYRight(info.toX);

        float Y1 = Math.min(Math.min(T_Y11, T_Y22), Math.min(T_Y21, T_Y12));
        float Y2 = Math.max(Math.max(T_Y11, T_Y22), Math.max(T_Y21, T_Y12));

        info.actor.setBounds(getX() + X1, getY() + Y1, X2 - X1, Y2 - Y1);
    }


}