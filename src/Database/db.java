package Database;

import java.sql.*;

public class db {
    private static Connection connection;
    private static final String USERNAME = "U060Lx";
    private static final String PASSWORD = "53688666169";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://3.227.166.251/U060Lx";

    public static Connection getConnection() {
        return connection;
    }

    public static void connect() {
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println("Database connected");
        }
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println("Database disconnected");
        }
    }
}
