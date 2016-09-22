package tommon.managers;

import tommon.annotations.KeyValueMonitor;
import tommon.annotations.KeyValueObject;
import tommon.plugins.PluginConfig;
import tommon.plugins.timers.KeyValuePluginTimer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by majlen on 4.7.16.
 */
public class KeyValueManager {
    public static PluginConfig loadPlugin(String name, Class clazz, StorageManager storage) {
        KeyValuePluginTimer timer = new KeyValuePluginTimer(clazz, storage);
        PluginConfig config = new PluginConfig(timer, name);

        if (clazz.isAnnotationPresent(KeyValueObject.class)) {
            KeyValueObject ann = (KeyValueObject)clazz.getAnnotation(KeyValueObject.class);
            config.setTable(ann.table());
        } else {
            System.out.println("Wrong annotation.");
            return null;
        }

        Field[] fs = clazz.getDeclaredFields();
        List<String> l = new ArrayList<String>(fs.length);

        for (Field f : fs) {
            if (f.isAnnotationPresent(KeyValueMonitor.class)) {
                l.add(f.getName());
            }
        }

        config.setFields(l.toArray(new String[0]));
        System.out.println("INFO: loaded KeyValue plugin "+name);
        return config;
    }
}
