package em.demonorium.timetable.Utils.AlignedGroup;

public class ActorSize {
    private Float weight;
    private Float maxSize;

    private Float currentSize;
    private boolean isMaxSize;

    public ActorSize(Float weight) {
        this.weight = weight;
    }

    public ActorSize(Float weight, Float maxSize) {
        this.weight = weight;
        this.maxSize = maxSize;
    }

    public Float getWeight() {
        return weight;
    }

    public Float getMaxSize() {
        return maxSize;
    }

    public Float getCurrentSize() {
        return currentSize;
    }

    public boolean isMaxSize() {
        return isMaxSize;
    }

    void calcSize(Float totalWeight, Float size) {
        this.currentSize = size * (weight / totalWeight);
        if ((maxSize == null) || (this.currentSize < maxSize)) {
            isMaxSize = false;
        }
        else {
            isMaxSize = true;
            currentSize = maxSize;
        }
    }
}
