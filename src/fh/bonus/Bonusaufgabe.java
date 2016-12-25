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
            System.out.print("Eingabe: ");

            try {
                selection = Integer.parseInt(SCANNER.nextLine());
                switch (selection) {
                    case 1:
                        System.out.println("Auslast anzeigen");
                        auslast();
                        break;

                    case 2:
                        System.out.println("Lieferer hinzufügen");
                        addLieferer();
                        break;

                    case 3:
                        System.out.println("Bezirk ändern");
                        changeBezirk();
                        break;

                    case 4:
                        System.out.println("Exit");
                        break;

                    default:
                        System.out.println("FEHLER: Bitte geben Sie eine gültige Zahl ein!");
                        break;
                }
            } catch (NumberFormatException ex) {
                System.err.println(ex);
                System.out.println("FEHLER: Es dürfen nur Zahlen eingegeben werden!");
            } finally {
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
        Connection conn = createConn();
        System.out.println("Bitte geben sie die Postleitzahl an: ");
        int input = Integer.parseInt(SCANNER.nextLine());
        try {
            String sqlString = "SELECT COUNT(lieferer.idLieferer) as 'Lieferer', "
                    + "(Select count(bestellung.idBestellung) "
                    + "from bestellung join liefererbestaetigung ON bestellung.idBestellung = liefererbestaetigung.Bestellung_idBestellung "
                    + "where liefererbestaetigung.Lieferer_idLieferer = lieferer.idLieferer "
                    + "and bestellung.bestellstatus = 'abgeschlossen') as Bestellungen "
                    + "FROM `lieferer_lieferbezirk` " // liefert Anzahl abgeschlossener Bestellungen und Lieferer f�r das Bezirk
                    + "INNER JOIN lieferbezirk "
                    + "ON lieferbezirk.idLieferbezirk = lieferer_lieferbezirk.Lieferbezirk_idLieferbezirk "
                    + "INNER JOIN lieferer "
                    + "on lieferer.idLieferer = lieferer_lieferbezirk.Lieferer_idLieferer "
                    + "WHERE lieferbezirk.plz = ?";
            PreparedStatement stmt = conn.prepareStatement(sqlString);
            stmt.setInt(1, input);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String lieferer = rs.getString("Lieferer");
                String bestellungen = rs.getString("Bestellungen");

                System.out.println("Liefer : " + lieferer);
                System.out.println("Bestellungen : " + bestellungen);
            }
            stmt.close();
            sqlString = "SELECT avg(anzahl*preis) as Preis "
                    + "FROM bestellposition "
                    + "JOIN artikel ON bestellposition.Artikel_idArtikel = artikel.idArtikel " // TODO Liefert zwar den Durchschnisspreis, aber von allen Bestellungen hier muss eingegrenzt werden nach der PLZ

                    + "JOIN bestellung ON bestellung.idBestellung = bestellposition.Bestellung_idBestellung "
                    + "WHERE bestellstatus = 'abgeschlossen'";

            stmt = conn.prepareStatement(sqlString);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String preis = rs.getString("Preis");
                System.out.println("Preis : " + preis);
            }
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.err.println("*** Exception:\n" + e);
        }
    }
}
