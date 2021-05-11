package em.demonorium.timetable.TimeData.Models;

import em.demonorium.timetable.TimeData.DataAPI;
import em.demonorium.timetable.TimeData.DataBase.Entry;
import em.demonorium.timetable.TimeData.Prototypes;

public class LessonModel implements Model {
    public int subject = -1;
    public String auditory = "";
    public String building = "";


    public LessonModel() {
    }
    public LessonModel(Entry entry) {
        fromEntry(entry);
    }

    public LessonModel(int selectedSubject, String text, String text1) {
        this.subject = selectedSubject;
        this.auditory = text;
        this.building = text1;
    }


    public boolean isSpace() {
        return subject == -1;
    }



    public void fromEntry(Entry entry) {
        subject = entry.get("subject");
        auditory = entry.get("auditory");
        building = entry.get("building");
    }

    public Entry toEntry() {
        Entry entry = DataAPI.base().newEntry(Prototypes.Lesson.prototype);
        toEntry(entry);
        return entry;
    }

    @Override
    public void toEntry(Entry entry) {
        entry.set("subject", subject);
        entry.set("auditory", auditory);
        entry.set("building", building);
    }


}
