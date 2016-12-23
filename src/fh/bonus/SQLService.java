package fh.bonus;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ExaShox
 */
public class SQLService {
    private Connection con = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public SQLService () {
        con  = ConnectionManager.getConnection();
        stmt  = con.createStatement();
        rs  = stmt.executeQuery(sql);
    }
}