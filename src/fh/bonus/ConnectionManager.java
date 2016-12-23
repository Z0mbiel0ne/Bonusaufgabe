package fh.bonus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manager for SQL-Connection
 * 
 * @author ExaShox
 */
public class ConnectionManager {
    private final static String 
            URL = "jdbc:mysql://localhost:3306/",
            DATABASE = "venenumbonus",
            DRIVER = "com.mysql.jdbc.Driver",
            USER = "root",
            PASSWORD = "";
    private static Connection conn;

    public static Connection getConnection() {
        try {
            Class.forName(DRIVER);
            try {
                conn = DriverManager.getConnection(URL + DATABASE, USER, PASSWORD);
            } catch (SQLException ex) {
                System.err.println(ex);
                System.out.println("ERROR: Failed to create the database connection.");
            }
        } catch (ClassNotFoundException ex) {
            System.err.println(ex);
            System.out.println("FATAL ERROR: Driver not found.");
        }
        return conn;
    }
}
