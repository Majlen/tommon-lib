package tommon.plugins;

import tommon.plugins.timers.Timers;

/**
 * Created by majlen on 20.7.16.
 */
public class PluginConfig {
    private Timers timer;
    private String table;
    private String name;
    private String[] fields;

    public PluginConfig(Timers timer, String name) {
        this.timer = timer;
        this.name = name;
    }

    public Timers getTimer() {
        return timer;
    }

    public void setTimer(Timers timer) {
        this.timer = timer;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }
}
