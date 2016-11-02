package tommon.managers;

import java.io.PrintWriter;
import java.time.Instant;
import java.util.Map;

public interface StorageManager {

	void close() throws Exception;

	void addTable(String table, String[] columns) throws Exception;

	void addRow(String table, String[] columns, String[] values) throws Exception;

	@Deprecated
	void printTableCSV(String table, String[] columns, Instant from, Instant to, PrintWriter print) throws Exception;

	int getMinimumDate(String table) throws Exception;

	Map<Instant, String> getColumn(String table, String column, Instant from, Instant to) throws Exception;

	Map<Instant, Map<String, String>> getColumns(String table, String[] columns, Instant from, Instant to) throws Exception;
}
