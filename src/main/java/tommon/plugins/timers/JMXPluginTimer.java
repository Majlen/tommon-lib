package tommon.plugins.timers;

import tommon.annotations.JMXCompositeMonitor;
import tommon.annotations.JMXMonitor;
import tommon.annotations.JMXObject;
import tommon.managers.DBManager;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import java.lang.annotation.Annotation;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by majlen on 22.6.16.
 */
public class JMXPluginTimer extends Timers {
    private Timer timer;
    private String table;
    private String JMXname;
    private Map<Annotation, String> attr = new HashMap<Annotation, String>();

    public JMXPluginTimer(Class clazz) {
        super(0);

        int periodInMinutes;

        if (clazz.isAnnotationPresent(JMXObject.class)) {
            JMXObject ann = (JMXObject)clazz.getAnnotation(JMXObject.class);
            JMXname = ann.value();
            periodInMinutes = ann.period();
            table = ann.table();
        } else {
            return;
        }

        Field[] fs = clazz.getDeclaredFields();

        for (Field f : fs) {
            if (f.isAnnotationPresent(JMXMonitor.class)) {
                JMXMonitor ann = (JMXMonitor)f.getAnnotation(JMXMonitor.class);
                attr.put(ann, f.getName());
            } else if (f.isAnnotationPresent(JMXCompositeMonitor.class)) {
                JMXCompositeMonitor ann = (JMXCompositeMonitor)f.getAnnotation(JMXCompositeMonitor.class);
                attr.put(ann, f.getName());
            }
        }

        DBManager.addTable(table, attr.values().toArray(new String[0]));

        timer = new Timer();
        long period = 60000*periodInMinutes;
        timer.scheduleAtFixedRate(this, 200, period);
    }

    @Override
    public void run() {
        List<String> columns = new ArrayList<String>();
        List<String> values = new ArrayList<String>();

        try {
            ObjectName name = new ObjectName(JMXname);
            MBeanServer mbean = ManagementFactory.getPlatformMBeanServer();

            //If the ObjectName contains wildcard, get the first ObjectName that satisfies
            if (name.isPattern()) {
                Set<ObjectName> set = mbean.queryNames(name, null);
                name = set.iterator().next();
            }

            for (Map.Entry<Annotation, String> entry : attr.entrySet()) {
                Annotation ann = entry.getKey();
                columns.add(entry.getValue());

                //Resolve CompositeData if necessary
                if (ann instanceof JMXMonitor) {
                    JMXMonitor jmxMonitor = (JMXMonitor)ann;
                    values.add(mbean.getAttribute(name, jmxMonitor.value()).toString());
                } else if (ann instanceof JMXCompositeMonitor) {
                    JMXCompositeMonitor jmxMonitor = (JMXCompositeMonitor)ann;
                    CompositeData data = (CompositeData)mbean.getAttribute(name, jmxMonitor.value());
                    if (data == null) {
                        values.add("0");
                    } else {
                        values.add(data.get(jmxMonitor.part()).toString());
                    }
                } else {
                    System.out.println("Unknown JMX annotation.");
                }
            }
        } catch (Exception e) {
            System.out.println("Cannot connect to JMX " + JMXname);
            System.out.println("ERROR: "+e.getClass().getName());
            System.out.println("ERROR: "+e.getMessage());
        }

        String[] typeRef = new String[0];
        DBManager.addRow(table, "OK", columns.toArray(typeRef), values.toArray(typeRef));
    }
}
