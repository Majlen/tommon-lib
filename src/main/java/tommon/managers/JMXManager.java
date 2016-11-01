package tommon.managers;

import tommon.annotations.JMXCompositeMonitor;
import tommon.annotations.JMXMonitor;
import tommon.annotations.JMXObject;
import tommon.plugins.PluginConfig;
import tommon.plugins.timers.JMXPluginTimer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * JMX plugins manager providing loadPlugin method.
 * @author Milan Ševčík
 */
public class JMXManager {
	/**
	 * Loads JMX plugins.
	 * @param name name of the plugin
	 * @param clazz class representing the plugin
	 * @param storage implementation if StorageManager to use.
	 * @return plugins configuration
	 */
	public static PluginConfig loadPlugin(String name, Class clazz, StorageManager storage) {
		JMXPluginTimer timer = new JMXPluginTimer(clazz, storage);
		PluginConfig config = new PluginConfig(timer, name);

		if (clazz.isAnnotationPresent(JMXObject.class)) {
			JMXObject ann = (JMXObject) clazz.getAnnotation(JMXObject.class);
			config.setTable(ann.table());
		} else {
			System.out.println("Wrong annotation.");
			return null;
		}

		Field[] fs = clazz.getDeclaredFields();
		List<String> l = new ArrayList<String>(fs.length);

		for (Field f : fs) {
			if (f.isAnnotationPresent(JMXMonitor.class) || f.isAnnotationPresent(JMXCompositeMonitor.class)) {
				l.add(f.getName());
			}
		}

		config.setFields(l.toArray(new String[0]));
		System.out.println("INFO: loaded JMX plugin " + name);
		return config;
	}
}
