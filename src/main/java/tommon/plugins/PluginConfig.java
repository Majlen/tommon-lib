package tommon.plugins;

import java.util.Comparator;
import java.util.TimerTask;

/**
 * Plugin configuration
 * @author Milan Ševčík
 */
public class PluginConfig implements Comparator<PluginConfig> {
	private TimerTask timer;
	private String table;
	private String name;
	private String[] fields;

	/**
	 * Empty constructor necessary for implementing Comparator interface
	 */
	public PluginConfig() {
	}

	/**
	 * Constructor
	 * @param timer used Timers extension
	 * @param name name of the plugin
	 */
	public PluginConfig(TimerTask timer, String name) {
		this.timer = timer;
		this.name = name;
	}

	public TimerTask getTimer() {
		return timer;
	}

	public void setTimer(TimerTask timer) {
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
