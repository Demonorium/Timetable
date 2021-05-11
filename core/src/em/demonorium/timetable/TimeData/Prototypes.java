package em.demonorium.timetable.TimeData;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.TreeMap;

import em.demonorium.timetable.Core;
import em.demonorium.timetable.TimeData.DataBase.DataBase;
import em.demonorium.timetable.TimeData.DataBase.Entry;
import em.demonorium.timetable.TimeData.DataBase.EntryPrototype;
import em.demonorium.timetable.Utils.SharedCollection.Default.MapAdapter;
import em.demonorium.timetable.Utils.SharedCollection.Default.SharedList;
import em.demonorium.timetable.Utils.SharedCollection.SharedCollection;

public final class Prototypes {
    static EntryPrototype linkToTimetable = new EntryPrototype();
    static EntryPrototype baseObject = new EntryPrototype();


    public static class Task {
        public static final EntryPrototype prototype = new EntryPrototype();


        public String getName(Entry task) {
            return task.get("name");
        }
        public String getDescription(Entry task) {
            return task.get("basicDescription");
        }

    }


    public static class Day {
        public static final EntryPrototype prototype = new EntryPrototype();

        public static void setStandardTimetable(Entry day, DataBase base) {
            applyTimetable(day, (SharedList<SimpleDate>)base.get("standardTimetable"));
            day.set("isTimetableStandard", true);
        }

        public static void applyTimetable(Entry day, SharedList<SimpleDate> timetable) {
            SharedList<SimpleDate> daytt = day.get("timetable");
            daytt.clear();

            for (SimpleDate simpleDate: timetable) {
                daytt.add(new SimpleDate(simpleDate));
            }
            day.set("isTimetableStandard", false);
        }


        public static void clearLessonList(Entry day) {
            SharedList<Entry> lessons = day.get("lessons");
            for (Entry lesson: lessons) {
                Subject.unlinkLesson(day.BASE.get((Integer)lesson.get("subject")), lesson.ID);
            }
            lessons.clear();
        }

        public static boolean isHoliday(Entry day) {
            SharedList<Entry> lessons = day.get("lessons");
            int count = 0;

            for (int i = lessons.size() - 1; i >= 0; --i)
                if (Lesson.isSpace(lessons.get(i)))
                    ++count;
                else
                    break;

            count = lessons.size() - count;
            return count == 0;
        }

        public static void applyDay(Entry day1, Entry day2) {
            clearLessonList(day1);
            applyTimetable(day1, (SharedList<SimpleDate>)day2.get("timetable"));

            SharedList<Entry> lessons = day1.get("lessons");
            SharedList<Entry> newLessons = day2.get("lessons");

            for (Entry lesson: newLessons) {
                Entry subject = lesson.BASE.get((Integer)lesson.get("subject"));
                Subject.unlinkLesson(subject, lesson.ID);
                lessons.add(lesson);
                Subject.linkLesson(subject, lesson, lessons);
            }
            newLessons.clear();
        }


        public static void addLesson(Entry day, Entry lesson) {
            SharedList<Entry> lessons = day.get("lessons");
            lessons.add(lesson);
            Entry subject = lesson.BASE.get((Integer)lesson.get("subject"));
            Subject.linkLesson(subject, lesson, lessons);
        }


        public static int getStart(int id) {
            return  id*2;
        }

        public static int getEnd(int id) {
            return getStart(id) + 1;
        }

        public static int possibleLessonCount(int timetable) {
            return (int)Math.floor(timetable / 2f);
        }
    }
    static class NullFactory<T> implements EntryPrototype.Factory<T> {
        @Override
        public T get() {
            return null;
        }
    }


    public static class Subject {
        public static final EntryPrototype prototype = new EntryPrototype();

        public static Entry unlinkLesson(Entry subject, Integer id) {
            SharedCollection<Integer, Entry> collection = subject.get("lessonList");
            Entry link = collection.get(id);
            collection.remove(id);
            return link;
        }
        public static void linkLesson(Entry subject, Entry lesson, SharedList<Entry> list) {
            SharedCollection<Integer, Entry> collection = subject.get("lessonList");
            Entry link = subject.newEntry(linkToTimetable);
            link.set("lesson", lesson);
            link.set("list", list);
            collection.add(lesson.ID, link);
        }
    }

    public static class Lesson {
        public static final EntryPrototype prototype = new EntryPrototype();

        public static void setSpace(Entry lesson) {
            lesson.set("subject", -1);
        }
        public static boolean isSpace(Entry lesson) {
            return (Integer)lesson.get("subject") == -1;
        }

        public static void setSubject(Entry lesson, SharedList<Entry> lessons, int subjectID) {
            if (!isSpace(lesson)) {
                Entry subject = lesson.BASE.get(subjectID);
                lesson.BASE.destroy(Subject.unlinkLesson(subject, lesson.ID).ID);
            }

            lesson.set("subject", subjectID);
            if (isSpace(lesson)) return;

            Entry subject = lesson.BASE.get(subjectID);
            Subject.linkLesson(subject, lesson, lessons);
        }
    }

    public static void init() {
        baseObject.register("selectedDate", new EntryPrototype.Factory<GregorianCalendar>() {
            @Override
            public GregorianCalendar get() {
                return (GregorianCalendar) Core.getCurrentDate().clone();
            }
        });
        baseObject.register("tasks", new SharedCollection<GregorianCalendar, SharedList<Entry>>(new TreeMap<>()));
        baseObject.register("changes", new SharedCollection<Integer, Entry>(new TreeMap<>()));
        baseObject.register("subjects", new SharedList<>(new ArrayList<Entry>()));
        baseObject.register("weeks", new ArrayList<ArrayList<Entry>>());
        baseObject.register("standardTimetable", new SharedList<SimpleDate>(new ArrayList<>()));
        baseObject.register("firstWeek", 0);

        Subject.prototype.register("name", "Название");
        Subject.prototype.register("teacher", "Преподаватель");
        Subject.prototype.register("exam", "none");
        Subject.prototype.setConstructor("name", "teacher", "exam");
        Subject.prototype.register("lessonList", new SharedCollection<>(new MapAdapter<>(new TreeMap<Integer, Entry>())));
        Subject.prototype.setDeconstruction(new EntryPrototype.EntryAction() {
            @Override
            public void action(Entry entry, DataBase base) {
                SharedCollection<Integer, Entry> collection = entry.get("lessonList");
                for (Entry e: collection) {
                    Entry lesson = e.get("lesson");
                    Lesson.setSpace(lesson);
                    ((SharedCollection)e.get("list")).updateByValue(lesson);
                }
            }
        });


        Lesson.prototype.register("subject", -1);
        Lesson.prototype.register("auditory", "");
        Lesson.prototype.register("building", "");

        Lesson.prototype.setDeconstruction(new EntryPrototype.EntryAction() {
            @Override
            public void action(Entry entry, DataBase base) {
                if (!Lesson.isSpace(entry)) {
                    Entry subject = base.get((Integer)entry.get("subject"));
                    base.destroy(Subject.unlinkLesson(subject, entry.ID).ID);
                }
            }
        });

        Day.prototype.register("lessons", new EntryPrototype.Factory() {
            @Override
            public Object get() {
                return new SharedList<>(new LinkedList<Entry>());
            }
        });
        Day.prototype.register("timetable", new EntryPrototype.Factory<SharedList<SimpleDate>>() {

            @Override
            public SharedList<SimpleDate> get() {
                if (DataAPI.isLoaded()) {
                    SharedList<SimpleDate> sharedList = new SharedList<>(new ArrayList<>());
                    SharedList<SimpleDate> tt = DataAPI.getStandardTimetable();

                    for (SimpleDate date: tt)
                        sharedList.add(new SimpleDate(date));

                    return sharedList;
                }


                return new SharedList<>(new ArrayList<>());
            }
        });
        Day.prototype.register("isTimetableStandard", true);
        Day.prototype.setDeconstruction(new EntryPrototype.EntryAction() {
            @Override
            public void action(Entry entry, DataBase base) {
                SharedList<Entry> lessons = entry.get("lessons");
                for (Entry lesson: lessons)
                    base.destroy(lesson.ID);
            }
        });

        linkToTimetable.register("lesson", new NullFactory<Entry>());
        linkToTimetable.register("list", new NullFactory<ArrayList<Entry>>());

        Task.prototype.register("name", "Задание");
        Task.prototype.register("content", new SharedList<VisibleObject>(new ArrayList<>()));
        Task.prototype.register("date", new GregorianCalendar());
        Task.prototype.register("completed", false);
    }
}
