package fh.bonus;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SQL Service
 *
 * @author ExaShox
 */
public class SQLService {

    /**
     * Ordnet einem Lieferer einen neuen Bezirk zu.
     *
     * @param lieferbezirkId
     * @param liefererId
     */
    public void updateBezirk(int liefererId, int lieferbezirkId) {
        Connection conn = ConnectionManager.getConnection();
        CallableStatement stmt;

        try {
            String sql = "{CALL updateBezirk(?, ?, ?)}";
            stmt = conn.prepareCall(sql);
            stmt.setInt(1, liefererId);
            stmt.setInt(2, lieferbezirkId);
            stmt.registerOutParameter(3, java.sql.Types.VARCHAR);
            stmt.execute();

            String msg = stmt.getString(3);
            System.out.println(msg);
        } catch (SQLException ex) {
            Logger.getLogger(Bonusaufgabe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Fuegt einen neuen Lieferer hinzu
     * 
     * @param liefererId
     * @param vorname 
     */
    public void insertLieferer(int liefererId, String vorname) {
        Connection conn = ConnectionManager.getConnection();
        CallableStatement stmt;

        try {
            String sql = "{CALL insertLieferer(?, ?, ?)}";
            stmt = conn.prepareCall(sql);
            stmt.setInt(1, liefererId);            
            stmt.setString(2, vorname);
            stmt.registerOutParameter(3, java.sql.Types.VARCHAR);
            stmt.execute();

            String msg = stmt.getString(3);
            System.out.println(msg);
        } catch (SQLException ex) {
            Logger.getLogger(Bonusaufgabe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get data from Database
     *
     * @param sql
     * @return ResultSet
     * @throws SQLException
     */
    public ResultSet get(String sql) throws SQLException {
        ResultSet rs;
        try (Connection conn = ConnectionManager.getConnection();
                Statement stmt = conn.createStatement()) {
            rs = stmt.executeQuery(sql);
        }
        return rs;
    }

    /**
     * Set data from Database
     *
     * @param sql
     * @throws SQLException
     */
    public void set(String sql) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}
