package tommon.plugins;

import tommon.plugins.timers.Timers;

import java.util.Comparator;

public class PluginConfig implements Comparator<PluginConfig> {
	private Timers timer;
	private String table;
	private String name;
	private String[] fields;

	public PluginConfig() {
	}

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

	@Override
	public int compare(PluginConfig o1, PluginConfig o2) {
		return o1.getName().compareTo(o2.getName());
	}
}
