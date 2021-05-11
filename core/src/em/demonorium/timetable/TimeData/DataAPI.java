package em.demonorium.timetable.TimeData;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import em.demonorium.timetable.Core;
import em.demonorium.timetable.TimeData.DataBase.DataBase;
import em.demonorium.timetable.TimeData.DataBase.Entry;
import em.demonorium.timetable.TimeData.Models.DayModel;
import em.demonorium.timetable.Utils.SharedCollection.Default.SharedList;
import em.demonorium.timetable.Utils.SharedCollection.SharedCollection;
import em.demonorium.timetable.Utils.VariableSystem.VariableSystem;

public class DataAPI {
    public static DataBase object;
    private static boolean loaded = false;


    public static DataBase base() {
        return object;
    }

    public static boolean isLoaded() {
        return loaded;
    }



    public static int getChangesDay(GregorianCalendar calendar) {
        return calendar.get(Calendar.DAY_OF_YEAR)*10000 + calendar.get(Calendar.YEAR);
    }

    public static GregorianCalendar getCalendarFromChangesDay(int day) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_YEAR, day / 10000);
        calendar.set(Calendar.YEAR, day % 10000);
        return calendar;
    }

    public static void addChanges(GregorianCalendar calendar, DayModel model) {
        int dayToUpdate = getChangesDay(calendar);
        SharedCollection<Integer, Entry> changes = getChanges();
        Entry dd = changes.get(dayToUpdate);

        if (dd == null) {
            changes.add(dayToUpdate, model.toEntry());
        } else {
            Prototypes.Day.applyDay(dd, model.toEntry());
            changes.update(dayToUpdate);
        }
    }
    public static Entry getSelectedDay() {
        return getDay(getSelectedDate());
    }

    public static Entry getNextDay() {
        return getDay(getNextDate());
    }

    public static Entry getPrevDay() {
        return getDay(getPrevDate());
    }


    public static GregorianCalendar getNextDate() {
        GregorianCalendar calendar = cloneSelectedDate();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return calendar;
    }
    public static GregorianCalendar getPrevDate() {
        GregorianCalendar calendar = cloneSelectedDate();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendar;
    }

    public static GregorianCalendar cloneSelectedDate() {
        return (GregorianCalendar) getSelectedDate().clone();
    }


    public static void selectDate(GregorianCalendar calendar) {
        object.set("selectedDate", calendar);
    }

    public static void addToDate(int days) {
        getSelectedDate().add(Calendar.DAY_OF_YEAR, days);
    }

    public static void leftDate() {
        addToDate(-1);
    }

    public static void rightDate() {
        addToDate(1);
    }

    public static SharedCollection<Integer, Entry> getChanges() {
        return object.get("changes");
    }


    public static int getWeekNumber(GregorianCalendar calendar) {
        if (getWeeksCount() == 0)
            return 0;

        long delta = calendar.get(Calendar.WEEK_OF_YEAR) - (Integer)object.get("firstWeek");
        int week = (int) (delta % (getWeeksCount()));
        if (week < 0)
            week = -week + (getWeeksCount() - 2);
        return week;
    }

    public static ArrayList<Entry> getWeek(GregorianCalendar calendar) {
        if (getWeeks().isEmpty())
            return null;

        return getWeeks().get(getWeekNumber(calendar));
    }

    public static ArrayList<ArrayList<Entry>> getWeeks() {
        return object.get("weeks");
    }

    public static int getWeeksCount() {
        return getWeeks().size();
    }

    public static int getDayOfWeek(GregorianCalendar calendar) {
        return (calendar.get(Calendar.DAY_OF_WEEK)+5)%7;
    }

    public static Entry getDay(GregorianCalendar calendar) {
        int chDay = getChangesDay(calendar);
        if (getChanges().containsKey(chDay))
            return getChanges().get(chDay);

        if (getWeeks().isEmpty())
            return null;

        return getWeek(calendar).get(getDayOfWeek(calendar));
    }

    public static SharedList<SimpleDate> getStandardTimetable() {
        return object.get("standardTimetable");
    }


    public static GregorianCalendar getSelectedDate() {
        return object.get("selectedDate");
    }




    public static void setWeek(int currentWeek) {
        object.set("firstWeek", Core.getCurrentDate().get(Calendar.WEEK_OF_YEAR) - currentWeek);
    }


    public static class subjects {

        public static void add(String name, String teacher, String exam) {
            Entry entry = object.newEntry(Prototypes.Subject.prototype,
                    name, teacher, exam);
            ((SharedList<Entry>)object.get("subjects")).add(entry);
        }


        public static Entry get(int id) {
            return object.get(id);
        }

    }



    public static void load(String filename) throws IOException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        FileHandle handle = Gdx.files.local(filename);
        VariableSystem data;

        if (handle.exists() && !handle.isDirectory()) {
            FileInputStream stream = new FileInputStream(handle.file());
            ObjectInputStream input = new ObjectInputStream(stream);

            data = (VariableSystem) input.readObject();
            object = data.getValue("data");
        } else {
            data = new VariableSystem();
            object = new DataBase(Prototypes.baseObject);
        }



        if (data.testFlag("updateTime")) {
            selectDate((GregorianCalendar) Core.getCurrentDate().clone());
        }

        loaded = true;
    }

    public static void save(String filename, boolean update) throws IOException, NoSuchFieldException, IllegalAccessException {
        FileHandle handle = Gdx.files.local(filename);

        FileOutputStream stream = new FileOutputStream(handle.file());
        ObjectOutputStream output = new ObjectOutputStream(stream);

        VariableSystem vars = new VariableSystem();
        vars.setFlag("updateTime", update);

        vars.set("data", object);

        output.writeObject(vars);
    }
}
