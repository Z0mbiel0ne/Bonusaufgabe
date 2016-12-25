package fh.bonus;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * SQL Service
 * 
 * @author ExaShox
 */
public class SQLService {
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
    
    /**
     * Get data from Database
     * 
     * @param sql
     * @return ResultSet
     * @throws SQLException 
     */
    public ResultSet get(String sql) throws SQLException {
        conn  = ConnectionManager.getConnection();
        stmt  = conn.createStatement();
        rs  = stmt.executeQuery(sql);
        
        stmt.close();
        conn.close();
        return rs;
    }
    
    /**
     * Set data from Database
     * 
     * @param sql
     * @throws SQLException 
     */
    public void set(String sql) throws SQLException {
        conn  = ConnectionManager.getConnection();
        stmt  = conn.createStatement();
        stmt.executeUpdate(sql);
        
        stmt.close();
        conn.close();
    }
}