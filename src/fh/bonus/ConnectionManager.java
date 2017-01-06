package fh.bonus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Managment for SQL-Connection
 * 
 * @author ExaShox
 */
public class ConnectionManager {
    private final static String 
            URL = "jdbc:mysql://dd23226.kasserver.com:3306/",
            DATABASE = "d024ad18",
            DRIVER = "com.mysql.jdbc.Driver",
            USER = "d024ad18",
            PASSWORD = "SRU8VTc9HyPNNZb3";
    private static Connection conn;

    /**
     * Get Database Connection
     * 
     * @return Connection
     */
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