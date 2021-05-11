package em.demonorium.timetable.TimeData.Models;

import java.util.ArrayList;
import java.util.LinkedList;

import em.demonorium.timetable.TimeData.DataAPI;
import em.demonorium.timetable.TimeData.DataBase.Entry;
import em.demonorium.timetable.TimeData.Prototypes;
import em.demonorium.timetable.TimeData.SimpleDate;
import em.demonorium.timetable.Utils.SharedCollection.Default.SharedList;

public class DayModel implements Model {
    public SharedList<LessonModel> lessons = new SharedList<>(new ArrayList<>());
    public SharedList<SimpleDate> timetable = new SharedList<>(new LinkedList<>());
    public boolean standard = true;

    @Override
    public void fromEntry(Entry entry) {
        lessons.clear();
        timetable.clear();

        if (entry == null) {
            if (DataAPI.isLoaded()) {
                SharedList<SimpleDate> tt = DataAPI.getStandardTimetable();
                if (tt != null) {
                    applyTimetable(tt);
                    standard = true;
                }
                else
                    standard = false;
            }
            else
                standard = false;
            return;
        }

        for (SimpleDate date: (SharedList<SimpleDate>)entry.get("timetable"))
            timetable.add(date);
        standard = entry.get("isTimetableStandard");

        for (Entry entry1: (SharedList<Entry>)entry.get("lessons"))
            lessons.add(new LessonModel(entry1));



    }

    public void applyTimetable(SharedList<SimpleDate> newTimetable) {
        timetable.clear();
        if (newTimetable == null) {
            lessons.clear();
            return;
        }
        for (SimpleDate date: newTimetable)
            timetable.add(new SimpleDate(date));

        for (int i = lessons.size() - 1; i >= 0; --i) {
            if (i >= Prototypes.Day.possibleLessonCount(timetable.size()))
                lessons.remove(i);
        }

        standard = false;
    }


    @Override
    public Entry toEntry() {
        Entry entry = DataAPI.base().newEntry(Prototypes.Day.prototype);
        toEntry(entry);
        return entry;
    }

    @Override
    public void toEntry(Entry entry) {
        SharedList<Entry> _lessons = entry.get("lessons");

        for (Entry entry1: _lessons) {
//            if (!Prototypes.Lesson.isSpace(entry1))
//                Prototypes.Subject.unlinkLesson(DataAPI.base().get((Integer)entry1.get("subject")), entry1.ID);
            entry.BASE.destroy(entry1.ID);
        }
        _lessons.clear();


        if (standard)
            Prototypes.Day.setStandardTimetable(entry, entry.BASE);
        else
            Prototypes.Day.applyTimetable(entry, timetable);

        for (LessonModel model: lessons) {
            Entry lesson = model.toEntry();
            _lessons.add(lesson);
            if (Prototypes.Lesson.isSpace(lesson))
                continue;
            Prototypes.Subject.linkLesson(DataAPI.base().get(model.subject), lesson, _lessons);
        }


    }

    public boolean mayInsertLesson() {
        int count = 0;

        for (int i = lessons.size() - 1; i >= 0; --i)
            if (lessons.get(i).isSpace())
                ++count;
            else
                break;

        count = lessons.size() - count;
        return count < Prototypes.Day.possibleLessonCount(timetable.size());
    }
}
