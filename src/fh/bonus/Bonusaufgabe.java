package fh.bonus;

/**
 *
 * @author masch142, stkli002
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Bonusaufgabe {

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        int selection = 0;
        do {
            System.out.println("|==========================|");
            System.out.println("|        Verwaltung        |");
            System.out.println("|==========================|");
            System.out.println("| Optionen:                |");
            System.out.println("|        1. Auslast        |");
            System.out.println("|        2. add Lieferer   |");
            System.out.println("|        3. change Bezirk  |");
            System.out.println("|        4. Exit           |");
            System.out.println("|==========================|");

            System.out.println("");
            System.out.println("Eingabe:");
            // TODO Handle NumberFormatException
            selection = Integer.parseInt(scanner.nextLine());

            switch (selection) {
                case 1:
                    System.out.println("__________________");
                    System.out.println("|Auslast anzeigen|");
                    System.out.println("¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯");
                    auslast();
                    break;
                case 2:
                    System.out.println("_____________________");
                    System.out.println("|Lieferer hinzuf�gen|");
                    System.out.println("¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯");
                    addLieferer();
                    break;
                case 3:
                    System.out.println("_______________");
                    System.out.println("|Bezirk �ndern|");
                    System.out.println("¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯");
                    changeBezirk();
                    break;
                case 4:
                    System.out.println("______");
                    System.out.println("|Exit|");
                    System.out.println("¯¯¯¯¯¯");
                    break;
                default:
                    System.out.println("Bitte geben sie eine G�ltige Zahl ein!");
                    break;

            }
        } while (selection != 4);
        SCANNER.close();
    }

    private static Connection createConn() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://127.0.0.1:3306/venenumbonus";
            Connection conn = DriverManager.getConnection(url, "root", "");
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("*** Exception:\n" + e);
        }

        return null;
    }

    private static void changeBezirk() {
        Connection conn = createConn();
        System.out.println("Bitte geben sie die ID des Lieferers an: ");
        int id;
        System.out.println("Bitte geben sie die Postleitzahl an: ");
        int input = Integer.parseInt(SCANNER.nextLine());
        id = Integer.getInteger(SCANNER.nextLine());
        try {
            String sqlString = "UPDATE DBUSER SET USERNAME = ? WHERE USER_ID = ?";
            PreparedStatement stmt = conn.prepareStatement(sqlString);
            stmt.setInt(1, input);
            stmt.executeUpdate();

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("*** Exception:\n" + e);
        }
    }

    private static void addLieferer() {
        Connection conn = createConn();
        try {
            conn.close();
        } catch (SQLException e) {
            System.err.println("*** Exception:\n" + e);
        }
    }

    private static void auslast() {

        try {
            Connection conn = createConn();
            String sqlString = "select lieferbezirk.plz from lieferbezirk";

            PreparedStatement stmt = conn.prepareStatement(sqlString);
            ResultSet rs = stmt.executeQuery();

            System.out.println("Vorhandene Bezirke:");

            while (rs.next()) {
                System.out.println(rs.getInt("plz"));
            }

            System.out.println("");

            System.out.println("Bitte geben sie die Postleitzahl an: ");
            int input = Integer.parseInt(scanner.nextLine());

            sqlString = "SELECT "
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
            stmt = conn.prepareStatement(sqlString);
            stmt.setInt(1, input);
            rs = stmt.executeQuery();

            System.out.println("");
            while (rs.next()) {
                String lieferer = rs.getString("Lieferer");
                String bestellungen = rs.getString("Bestellungen");
                String preis = rs.getString("Preis");

                System.out.println("Liefer : " + lieferer);
                System.out.println("Bestellungen : " + bestellungen);
                System.out.println("Preis : " + preis);
            }
            stmt.close();

        } catch (SQLException e) {
            System.err.println("*** Exception:\n" + e);
        }
    }
}
