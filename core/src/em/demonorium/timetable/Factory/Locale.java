package em.demonorium.timetable.Factory;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import em.demonorium.timetable.Core;

public class Locale {
    private static HashMap<String, String> local = new HashMap<>();


    public static String get(String id) {
        if (local.containsKey(id))
            return local.get(id);

        Core.LOGGER.log(Level.WARNING, "No lcl key: " + id);
        return "er:" + id;
    }

    public static void register(String id, String name) {
        local.put(id, name);
    }

}
