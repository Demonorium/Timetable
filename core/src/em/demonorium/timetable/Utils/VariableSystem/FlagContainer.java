package em.demonorium.timetable.Utils.VariableSystem;

public interface FlagContainer {
    /**
     * Возвращает значение флага
     * @param name название флага
     * @return значение
     */

    boolean testAndSet(String name, boolean v);

    boolean testFlag(String name);

    /**
     * Установить флагу значение
     * @param name название флага
     * @param value значение
     */
    void setFlag(String name, boolean value);
    /**
     * Установить флагу значение true
     * @param name название флага
     */
    void setFlag(String name);
}
