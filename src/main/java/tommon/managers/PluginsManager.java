package tommon.managers;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by majlen on 14.7.16.
 */
public class PluginsManager {
    public static ClassLoader getPluginsClassLoader(ServletContext context) {
        ArrayList<URL> jarList = new ArrayList<>();
        try {
            Properties properties = new Properties();
            properties.load(context.getResourceAsStream("/WEB-INF/tommon.properties"));
            String pluginsDir = properties.getProperty("plugins.directory");
            pluginsDir = pluginsDir.replace("${catalina.base}", System.getProperty("catalina.base"));

            File dir = new File(pluginsDir);
            File[] filesList = dir.listFiles();

            for (File file : filesList) {
                if (file.isFile() && file.getName().endsWith(".jar")) {
                    jarList.add(file.toURI().toURL());
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR: tommon.properties is non-existent or cannot be accessed");
            System.out.println(e.getMessage());
        }

        return URLClassLoader.newInstance(jarList.toArray(new URL[0]), context.getClassLoader());
    }
}
