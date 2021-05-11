package em.demonorium.timetable.TimeData.Models;

import java.util.GregorianCalendar;
import java.util.LinkedList;

import em.demonorium.timetable.TimeData.DataAPI;
import em.demonorium.timetable.TimeData.DataBase.Entry;
import em.demonorium.timetable.TimeData.Prototypes;
import em.demonorium.timetable.TimeData.VisibleObject;
import em.demonorium.timetable.Utils.SharedCollection.Default.SharedList;

public class TaskModel implements Model{
    public String name = "";
    public GregorianCalendar date = new GregorianCalendar();
    public SharedList<VisibleObject> content = new SharedList<>(new LinkedList<>());


    @Override
    public void fromEntry(Entry entry) {
        name = entry.get("name");
        date = entry.get("date");
        content.clear();

        SharedList<VisibleObject> desc = entry.get("content");
        for (VisibleObject object: desc)
            content.add(object);
    }

    @Override
    public Entry toEntry() {
        Entry entry = DataAPI.base().newEntry(Prototypes.Task.prototype);
        toEntry(entry);
        return entry;
    }

    @Override
    public void toEntry(Entry entry) {
        entry.set("name", name);
        entry.set("date", date.clone());

        SharedList<VisibleObject> desc = entry.get("content");
        desc.clear();
        for (VisibleObject object: content)
            desc.add(object);
    }
}
