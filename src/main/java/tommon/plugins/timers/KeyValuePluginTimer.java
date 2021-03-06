package tommon.plugins.timers;

import tommon.annotations.KeyValueMonitor;
import tommon.annotations.KeyValueObject;
import tommon.managers.StorageManager;
import tommon.verifiers.LocalhostAllowHostnameVerifier;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * TimerTask of KeyValue plugins.
 * @author Milan Ševčík
 */
public class KeyValuePluginTimer extends TimerTask {
	private Timer timer;
	private String table;
	private URL url;
	private String delimiter;
	private Map<String, String> attr = new HashMap<String, String>();
	StorageManager storage;

	/**
	 * Constructor.
	 * @param clazz class with plugins configuration
	 * @param storage StorageManager implementation to use
	 */
	public KeyValuePluginTimer(Class clazz, StorageManager storage) {
		this.storage = storage;
		int periodInMinutes;

		if (clazz.isAnnotationPresent(KeyValueObject.class)) {
			KeyValueObject ann = (KeyValueObject) clazz.getAnnotation(KeyValueObject.class);
			try {
				url = new URL(ann.url());
			} catch (MalformedURLException e) {
				System.out.println(e.getMessage());
				return;
			}
			periodInMinutes = ann.period();
			delimiter = ann.delimiter();
			table = ann.table();
		} else {
			return;
		}

		Field[] fs = clazz.getDeclaredFields();

		for (Field f : fs) {
			if (f.isAnnotationPresent(KeyValueMonitor.class)) {
				KeyValueMonitor ann = (KeyValueMonitor) f.getAnnotation(KeyValueMonitor.class);
				attr.put(ann.value(), f.getName());
			}
		}

		try {
			storage.addTable(table, attr.values().toArray(new String[0]));
		} catch (Exception e) {
			System.out.println("ERROR: Could not create new table");
			System.out.println(e.getMessage());
			return;
		}

		timer = new Timer();
		long period = 60000 * periodInMinutes;
		timer.scheduleAtFixedRate(this, 200, period);
	}

	@Override
	public void run() {
		List<String> columns = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		try {
			if (url.getProtocol().equals("https")) {
				HttpsURLConnection.setDefaultHostnameVerifier(new LocalhostAllowHostnameVerifier(HttpsURLConnection.getDefaultHostnameVerifier()));
			}
			URLConnection connection = url.openConnection();

			BufferedReader read = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String line;
			while ((line = read.readLine()) != null) {
				String[] l = line.split(delimiter);
				if (attr.keySet().contains(l[0])) {
					columns.add(attr.get(l[0]));
					values.add(l[1]);
				}
			}

			read.close();


		} catch (IOException e) {
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
		}

		String[] typeRef = new String[0];
		try {
			storage.addRow(table, columns.toArray(typeRef), values.toArray(typeRef));
		} catch (Exception e) {
			System.out.println("ERROR: Could not store values");
			System.out.println(e.getMessage());
		}
	}
}
