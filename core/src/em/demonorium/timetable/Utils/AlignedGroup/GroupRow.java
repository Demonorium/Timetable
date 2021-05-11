package em.demonorium.timetable.Utils.AlignedGroup;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;

public class GroupRow {
    private ActorSize heightProportion;
    private ActorSize[] columnWidthProportions;

    private float[] shifts;
    private float totalWeight;

    float position;


    public GroupRow(ActorSize heightProportion, ActorSize... columnWidthProportions) {
        this.heightProportion = heightProportion;
        this.columnWidthProportions = columnWidthProportions;
        calcTotalWeight();
    }

    public GroupRow(float heightProportion, float... columnWidthProportions) {
        this.heightProportion = new ActorSize(heightProportion);

        this.columnWidthProportions = new ActorSize[columnWidthProportions.length];
        for (int i = 0; i < this.columnWidthProportions.length; ++i) {
            this.columnWidthProportions[i] = new ActorSize(columnWidthProportions[i]);
        }
        calcTotalWeight();
    }

    public GroupRow(ActorSize heightProportion, float... columnWidthProportions) {
        this.heightProportion = heightProportion;
        this.columnWidthProportions = new ActorSize[columnWidthProportions.length];
        for (int i = 0; i < this.columnWidthProportions.length; ++i) {
            this.columnWidthProportions[i] = new ActorSize(columnWidthProportions[i]);
        }
        calcTotalWeight();
    }

    public GroupRow(float heightProportion, ActorSize... columnWidthProportions) {
        this.heightProportion = new ActorSize(heightProportion);
        this.columnWidthProportions = columnWidthProportions;
        calcTotalWeight();
    }

    private void calcTotalWeight() {
        totalWeight = 0;
        for (ActorSize size: columnWidthProportions) {
            totalWeight += size.getWeight();
        }
        shifts = new float[columnWidthProportions.length + 1];
    }

    void calcShifts(Float size) {
        float currentX = 0;
        float partialSize = 0;
        float fixedSize = 0;

        for (ActorSize sz: columnWidthProportions) {
            sz.calcSize(totalWeight, size);
            partialSize += sz.getCurrentSize();
            if (sz.isMaxSize())
                fixedSize += sz.getCurrentSize();
        }

        float mult = (size - fixedSize) / (partialSize - fixedSize);

        for (int i = 0; i < columnWidthProportions.length; ++i) {
            this.shifts[i] = currentX;
            if (columnWidthProportions[i].isMaxSize()) {
                currentX += columnWidthProportions[i].getCurrentSize();
            } else {
                currentX += columnWidthProportions[i].getCurrentSize() * mult;
            }
        }
        this.shifts[columnWidthProportions.length] = size;
    }

    float getLeftX(int index) {
        return shifts[index];
    }
    float getRightX(int index) {
        return shifts[index + 1];
    }

    ActorSize getHeightProportion() {
        return heightProportion;
    }



    public int getColumnsCount() {
        return columnWidthProportions.length;
    }
}
