package em.demonorium.timetable.Utils.Input;

public interface KeyboardInputAdapterFactory {
    public void register(String id, UniversalFieldStyle style);

    public KeyboardInputAdapter get(String id, String defaultValue);
}
