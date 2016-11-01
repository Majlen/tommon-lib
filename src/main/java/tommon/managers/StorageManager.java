package tommon.managers;

import java.io.PrintWriter;

public interface StorageManager {

	void close() throws Exception;

	void addTable(String table, String[] columns) throws Exception;

	void addRow(String table, String[] columns, String[] values) throws Exception;

	void printTableCSV(String table, String[] columns, int from, int to, PrintWriter print) throws Exception;

	int getMinimumDate(String table) throws Exception;
}
