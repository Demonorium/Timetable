package em.demonorium.timetable.TimeData.Models;

import em.demonorium.timetable.TimeData.DataAPI;
import em.demonorium.timetable.TimeData.DataBase.Entry;
import em.demonorium.timetable.TimeData.Prototypes;

public class SubjectModel implements Model {
    String teacher;
    String name;
    String isExam;

    @Override
    public void fromEntry(Entry entry) {
        teacher = entry.get("teacher");
        name = entry.get("name");
        isExam = entry.get("isExam");
    }

    @Override
    public Entry toEntry() {
        Entry entry = DataAPI.base().newEntry(Prototypes.Subject.prototype);
        toEntry(entry);
        return entry;
    }

    @Override
    public void toEntry(Entry entry) {
        entry.set("teacher", teacher);
        entry.set("name", name);
        entry.set("isExam", isExam);
    }
}
