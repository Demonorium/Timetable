package em.demonorium.timetable.Utils.Pools;

public class IDPool extends UniversalPool<Integer> {
    Integer max = 0;

    @Override
    public Integer get() {
        if (super.values.isEmpty())
            return max++;

        return super.get();
    }
}
