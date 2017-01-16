package fh.bonus;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SQL Service
 *
 * @author ExaShox
 */
public class SQLService {

    private final Scanner SCANNER = new Scanner(System.in);
    
    /**
     * Liefert Daten für die Auslast und gibt diese aus
     *
     * @param plz
     */
    public void auslast(int plz){
       Connection conn = ConnectionManager.getConnection();
        PreparedStatement stmt;
        try {
                     
            String sqlString = "SELECT "
                    + "(SELECT COUNT(lieferer_lieferbezirk.Lieferer_idLieferer) "
                    + "FROM lieferer_lieferbezirk "
                    + "WHERE lieferer_lieferbezirk.Lieferbezirk_idLieferbezirk = lieferbezirk.idLieferbezirk) AS Lieferer "
                    + ", "
                    + "(Select COUNT(bestellung.idBestellung) from bestellung "
                    + "inner join getraenkemarkt on bestellung.Getraenkemarkt_idGetraenkemarkt = getraenkemarkt.idGetraenkemarkt "
                    + "where bestellung.bestellstatus = 'abgeschlossen' and getraenkemarkt.plz = lieferbezirk.plz "
                    + ") as Bestellungen "
                    + ", "
                    + "(SELECT avg(anzahl*preis) "
                    + "FROM bestellposition "
                    + "JOIN artikel ON bestellposition.Artikel_idArtikel = artikel.idArtikel "
                    + "JOIN bestellung ON bestellung.idBestellung = bestellposition.Bestellung_idBestellung "
                    + "inner join getraenkemarkt on bestellung.Getraenkemarkt_idGetraenkemarkt = getraenkemarkt.idGetraenkemarkt "
                    + "WHERE bestellstatus = 'abgeschlossen' "
                    + "and getraenkemarkt.plz = lieferbezirk.plz) "
                    + "as Preis "
                    + "FROM lieferbezirk "
                    + "WHERE lieferbezirk.plz = ? ";
            
            stmt = conn.prepareStatement(sqlString); // Prepared Statement anlegen 
            
            stmt.setInt(1, plz); // Parameter setzen
            ResultSet rs = stmt.executeQuery(); // Query absetzen und ResultSet zurückholen

            System.out.println("");
            rs.next();
            if(!rs.getString("Lieferer").equals("0"))
            {
               do
               { 
                String lieferer = rs.getString("Lieferer");
                String bestellungen = rs.getString("Bestellungen"); // Daten aus ResultSet holen
                String preis = rs.getString("Preis");

                System.out.println("Liefer : " + lieferer);
                System.out.println("Bestellungen : " + bestellungen);
                System.out.println("Preis : " + preis);
               }
                while(rs.next());
            }
            else 
            {
                System.out.println("Es existiert keine Lieferer zu dieser Postleitzahl");
            }
            stmt.close();
        
        } catch (SQLException ex) {
            Logger.getLogger(Bonusaufgabe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Liefert alle vorhandenen Bezirke und gibt diese aus
     */
    public void getBezirke()
    {
        Connection conn = ConnectionManager.getConnection();
        PreparedStatement stmt; 
        try{           
            String sqlString = "select lieferbezirk.plz from lieferbezirk";
            stmt = conn.prepareStatement(sqlString); // Prepared Statement anlegen 
            ResultSet rs = stmt.executeQuery(); // Query absetzen und ResultSet zurückholen
            
            System.out.println("Vorhandene Bezirke:");
            
            while (rs.next()) { //Loop über Ergebnis
                System.out.println(rs.getInt("plz")); // Postleitzahlen ausgeben
            }
            
            System.out.println("");
        } catch (SQLException ex) {
            Logger.getLogger(Bonusaufgabe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
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
            stmt.setInt(1, liefererId); // Parameter setzen
            stmt.setInt(2, lieferbezirkId);
            stmt.registerOutParameter(3, java.sql.Types.VARCHAR);
            stmt.execute(); // Query absetzen

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
