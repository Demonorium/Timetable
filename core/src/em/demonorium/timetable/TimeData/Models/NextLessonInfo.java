package em.demonorium.timetable.TimeData.Models;


import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.crypto.Data;

import em.demonorium.timetable.TimeData.DataAPI;
import em.demonorium.timetable.TimeData.DataBase.Entry;
import em.demonorium.timetable.Utils.SharedCollection.Default.SharedList;
import em.demonorium.timetable.Utils.SharedCollection.SharedCollection;
import em.demonorium.timetable.Utils.SharedCollection.SimpleIterator;

import static java.lang.Math.abs;

public class NextLessonInfo {
    private TreeSet<Integer> weekShifts = new TreeSet<>();
    private TreeSet<GregorianCalendar> changes = new TreeSet<>();
    private TreeSet<GregorianCalendar> summary = new TreeSet<>();



    public void init(int subjectID) {
        changes.clear();
        weekShifts.clear();

        ArrayList<ArrayList<Entry>> weeks = DataAPI.getWeeks();
        int counter = 0;
        for (ArrayList<Entry> week: weeks) {
            for (Entry day: week) {
                SharedList<Entry> lessons = day.get("lessons");
                for (Entry lesson: lessons) {
                    if ((Integer)lesson.get("subject") == subjectID) {
                        weekShifts.add(counter);
                        break;
                    }
                }
                ++counter;
            }
        }


        SharedCollection<Integer, Entry> days = DataAPI.getChanges();
        for (SimpleIterator<Integer, Entry> it = days.iterator(); it.hasNext();) {
            Entry day = it.next();
            SharedList<Entry> lessons = day.get("lessons");
            for (Entry lesson: lessons) {
                if ((Integer)lesson.get("subject") == subjectID) {
                    changes.add(DataAPI.getCalendarFromChangesDay(it.key()));
                    break;
                }
            }
        }
    }


    private static GregorianCalendar makeCalendar(GregorianCalendar from, int shift) {
        GregorianCalendar clone = (GregorianCalendar)from.clone();
        clone.add(Calendar.DAY_OF_YEAR, shift - getIndex(from));
        return clone;
    }

    private GregorianCalendar aligned;
    public void alignFromDate(GregorianCalendar date, int interval) {
        summary.clear();
        summary.addAll(changes);
        aligned = date;

        if (weekShifts.isEmpty())
            return;

        int index = getIndex();
        if (weekShifts.contains(index)) {
            summary.add(aligned);
        } else if (weekShifts.size() == 1) {
            summary.add(makeCalendar(aligned, weekShifts.first()));
        } else {
            Integer containsNext = weekShifts.higher(index);
            if (containsNext == null) {
                Integer containerPrev = weekShifts.lower(index);
                summary.add(makeCalendar(aligned, containerPrev));
            }
        }
        extendBack(interval);
        extendNext(interval);
    }

    private int getIndex() {
        return getIndex(aligned);
    }

    private static int getIndex(GregorianCalendar date) {
        return DataAPI.getWeekNumber(date)*7+DataAPI.getDayOfWeek(date);
    }

    private void extendNext(int interval) {
        if (weekShifts.isEmpty())
            return;

        Integer totalShift = 0;
        Integer shift = weekShifts.higher(getIndex(aligned));

        for (int i = 0; i < interval; ++i) {
            if (shift == null) {
                shift = weekShifts.first();
                totalShift += 7*DataAPI.getWeeksCount();
            }
            summary.add(makeCalendar(aligned, totalShift + shift));
            shift = weekShifts.higher(shift);
        }

    }
    private void extendBack(int interval) {
        if (weekShifts.isEmpty())
            return;

        Integer totalShift = -7*DataAPI.getWeeksCount();
        Integer shift = weekShifts.lower(getIndex(aligned));

        for (int i = 0; i < interval; ++i) {
            if (shift == null) {
                shift = weekShifts.last();
                totalShift -= 7*DataAPI.getWeeksCount();
            }
            summary.add(makeCalendar(aligned, totalShift + shift));
            shift = weekShifts.higher(shift);
        }

    }

    private int deltaDays(Calendar a, Calendar b) {
        return b.get(Calendar.DAY_OF_YEAR) - a.get(Calendar.DAY_OF_YEAR);
    }



    public GregorianCalendar getNext(GregorianCalendar current) {
        if (current == null) return  null;
        GregorianCalendar newC = summary.higher(current);
        if (newC == null) {
            extendNext(10);
            newC = summary.higher(current);
        }
        return newC;
    }

    public GregorianCalendar getPrev(GregorianCalendar current) {
        if (current == null) return  null;
        GregorianCalendar newC = summary.lower(current);
        if (newC == null) {
            extendBack(10);
            newC = summary.lower(current);
        }
        return newC;
    }


    public GregorianCalendar getByShift(int shift) {
        if (shift > 0 ) {
            GregorianCalendar calendar = getNext(aligned);
            for (int i = 1; i < shift; ++i) {
                calendar = getNext(calendar);
                if (calendar == null)
                    return null;
            }
            return calendar;
        } else {
            GregorianCalendar calendar = getPrev(aligned);
            for (int i = 0; i < abs(shift); ++i) {
                calendar = getPrev(calendar);
                if (calendar == null)
                    return null;
            }
            return calendar;
        }
    }


}
