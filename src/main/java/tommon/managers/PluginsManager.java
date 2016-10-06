package tommon.managers;

import tommon.annotations.JMXObject;
import tommon.annotations.KeyValueObject;
import tommon.plugins.PluginConfig;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

/**
 * Created by majlen on 14.7.16.
 */
public class PluginsManager {
    public static PluginConfig[] getPlugins(ClassLoader parent, String pluginsDir, StorageManager storage) {
        ArrayList<PluginConfig> pluginList = new ArrayList<>();

        File dir = new File(pluginsDir);
        File[] filesList = dir.listFiles();

        for (File file : filesList) {
            if (file.isFile() && file.getName().endsWith(".jar")) {
                ClassLoader cl = null;
                try {
                    cl = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()}, parent);
                } catch (MalformedURLException e) {
                    System.out.println(e.getMessage());
                }

                Properties pluginProperties = new Properties();
                try {
                    pluginProperties.load(cl.getResourceAsStream("META-INF/plugins.properties"));
                    String[] plugins = pluginProperties.getProperty("plugins").split(",");
                    System.out.println(Arrays.toString(plugins));

                    for (String plugin : plugins) {
                        try {
                            Class clazz = Class.forName(plugin, true, cl);

                            Annotation[] anns = clazz.getAnnotations();
                            System.out.println(Arrays.toString(anns));
                            for (Annotation ann : anns) {
                                PluginConfig config = null;
                                if (ann.annotationType().equals(JMXObject.class)) {
                                    config = JMXManager.loadPlugin(plugin, clazz, storage);
                                } else if (ann.annotationType().equals(KeyValueObject.class)) {
                                    config = KeyValueManager.loadPlugin(plugin, clazz, storage);
                                } else {
                                    System.out.println("Unknown plugin annotation " + ann.toString());
                                }

                                if (config != null) {
                                    pluginList.add(config);
                                }
                            }
                        } catch (Exception e) {
                            java.lang.System.out.println("Unable to load plugin " + plugin);
                            System.out.println(e.getMessage());
                        }
                    }
                } catch (IOException e) {
                    System.out.println("ERROR: plugins.properties is non-existent. Malformed plugin.");
                    System.out.println(e.getMessage());
                }
            }
        }

        Collections.sort(pluginList, new PluginConfig());
        return pluginList.toArray(new PluginConfig[0]);
    }
}
