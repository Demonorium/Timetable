package em.demonorium.timetable.TimeData.Models;

import em.demonorium.timetable.TimeData.DataBase.Entry;

public interface Model {

    void fromEntry(Entry entry);
    Entry toEntry();
    void toEntry(Entry entry);

}
