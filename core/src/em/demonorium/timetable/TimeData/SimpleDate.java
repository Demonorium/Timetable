package em.demonorium.timetable.TimeData;

import java.io.Serializable;

public class SimpleDate implements Serializable {
    protected static final long serialVersionUID = 1L;

    int date;


    public static final int HOUR = 60;
    public static final int DAY = 24;
    public static final int MAX_DATE = HOUR*DAY;

    public SimpleDate() {
        date = 0;
    }

    public SimpleDate(int hours, int minutes) {
        this.date = hours*HOUR + minutes;
    }


    public SimpleDate(SimpleDate date) {
        this.date = date.date;
    }

    public SimpleDate(int date) {
        this.date = date;
        clip();
    }

    private void clip() {
        if (date >= MAX_DATE)
            date %= MAX_DATE;
        else if (date < 0) {
            date = (date + MAX_DATE) % MAX_DATE;
        }
    }


    public void add(int value) {
        date += value;
        clip();
    }

    public void subHour() {
        date -= HOUR;
        clip();
    }


    public void subMinute() {
        date -= 1;
        clip();
    }


    public void addHour() {
        date += HOUR;
        clip();
    }


    public void addMinute() {
        date += 1;
        clip();
    }



    public int getHours() {
        return (int)Math.floor(date / 60f);
    }

    public int getMinutes() {
        return date - getHours()*60;
    }

    public int getTime() {
        return date;
    }

    public static String tToStr(int value) {
        String v = Integer.toString(value);
        if (v.length() == 1)
            v = "0" + v;
        return v;
    }


    public String minutesToString() {
        return minutesToString(date);
    }

    public String hoursToString() {
        return hoursToString(date);
    }

    public static String minutesToString(int date) {
        return tToStr(new SimpleDate(date).getMinutes());
    }

    public static String hoursToString(int date) {
        return tToStr(new SimpleDate(date).getHours());
    }

    public static String getSimpleTime(int time) {
        return new SimpleDate(time).toString();
    }

    @Override
    public String toString() {
        return tToStr(getHours()) + ":" + tToStr(getMinutes());
    }
}
