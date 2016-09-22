package tommon.managers;

import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public final class DBManager {
    public static Connection sqlcon = null;
    private static Driver driver;

    public static Connection DBConnect(String dburl, String jdbc) throws ClassNotFoundException, SQLException {
        Class.forName(jdbc);
        sqlcon = DriverManager.getConnection(dburl);
        driver = DriverManager.getDriver(dburl);
        return sqlcon;
    }

    public static void DBDisconnect() throws SQLException {
        sqlcon.close();
        DriverManager.deregisterDriver(driver);
    }

    public static void addTable(String table, String[] columns) {
        String cols = deArray(columns);

        try {
            Statement s = sqlcon.createStatement();
            s.executeUpdate("CREATE TABLE IF NOT EXISTS "+ table+" (date INTEGER, msg TEXT"+cols+")");
        } catch (SQLException e) {
            System.out.println("Cannot create table in DB");
            System.out.println("ERROR: "+e.getMessage());
        }
    }

    public static void addRow(String table, String msg, String[] columns, String[] values) {
        String colsconv = deArray(columns);
        String valsconv = deArray(values);

        String cols = "(date, msg"+colsconv+")";
        String vals = "("+System.currentTimeMillis()/1000+", \""+msg+"\""+valsconv+")";

        try {
            Statement s = sqlcon.createStatement();
            s.executeUpdate("INSERT INTO "+table+" "+cols+" VALUES "+vals+";");
        } catch (Exception e) {
            System.out.println("Cannot insert data to DB: " + table +": " + msg);
            System.out.println(Arrays.toString(columns) + ": " + Arrays.toString(values));
            System.out.println("ERROR: "+e.getMessage());
        }
    }

    public static void printTableCSV(String table, String[] columns, int from, int to, PrintWriter print) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String cols = deArray(columns);

        try {
            Statement s = sqlcon.createStatement();
            ResultSet rs = s.executeQuery("SELECT date" + cols + " FROM "+ table +" WHERE date BETWEEN " + from + " AND " + to + ";");

            print.println("date"+cols);

            while (rs.next()) {
                print.print(df.format(new java.util.Date(rs.getInt("date") * 1000L)) + ",");

                for (int i = 0; i < columns.length; i++) {
                    print.print(rs.getString(columns[i]));
                    if (i == columns.length-1) {
                        print.print("\n");
                    } else {
                        print.print(",");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Cannot get data from DB");
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public static int getMinimumDate(String table) {
        int out = 0;
        try {
            Statement s = sqlcon.createStatement();
            ResultSet rs = s.executeQuery("SELECT min(date) AS date FROM " + table + ";");
            out = rs.getInt("date");
        } catch (Exception e) {
            System.out.println("Cannot get data from DB");
            System.out.println("ERROR: " + e.getMessage());
        }
        return out;
    }

    private static String deArray(String[] arr) {
        String out = "";
        for (String item : arr) {
            out += ", " + item;
        }
        return out;
    }
}
