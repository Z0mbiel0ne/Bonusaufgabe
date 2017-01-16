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
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bonusaufgabe {

    private static final SQLService SERVICE = new SQLService();
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        int selection;
        do {
            System.out.println("|==========================|");
            System.out.println("|        Verwaltung        |");
            System.out.println("|==========================|");
            System.out.println("| Optionen:                |");
            System.out.println("|        1. Auslast        |");
            System.out.println("|        2. add Lieferer   |"); // Menü darstellen
            System.out.println("|        3. change Bezirk  |");
            System.out.println("|        4. Exit           |");
            System.out.println("|==========================|");

            System.out.println("");
            System.out.println("Eingabe:");
            
            selection = scannInt(); // Menüauswahl einlesen
            switch (selection) {
                case 1: // bei Auslast
                    System.out.println("__________________");
                    System.out.println("|Auslast anzeigen|");
                    System.out.println("¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯");
                    auslast();
                    break;
                case 2: // Bei Lieferer hinzufügen
                    System.out.println("_____________________");
                    System.out.println("|Lieferer hinzuf�gen|");
                    System.out.println("¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯");
                    addLieferer();
                    break;
                case 3: // Bei Bezirk ändern
                    System.out.println("_______________");
                    System.out.println("|Bezirk ändern|");
                    System.out.println("¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯");
                    changeBezirk();
                    break;
                case 4: // Bei Ende
                    System.out.println("______");
                    System.out.println("|Exit|");
                    System.out.println("¯¯¯¯¯¯");
                    break;
                default: // Falsche Eingabe
                    System.out.println("Bitte geben Sie eine gültige Eingabe ein!");
                    break;


            }
        } while (selection != 4);

        SCANNER.close(); // Programm beenden und SCANNER schließen
    }
    
    private static void changeBezirk() {
        System.out.println("Bitte geben Sie die ID des Lieferers an: ");
        int idLieferer = scannInt(); // Eingabe einlesen
        System.out.println("Bitte geben Sie die ID des Lieferbezirkes an: ");
        int idLieferbezirk = scannInt(); // Eingabe einlesen

        SERVICE.updateBezirk(idLieferer, idLieferbezirk); // Service aufrufen
    }

    private static void addLieferer() {
        System.out.println("Bitte geben Sie die ID des Lieferers an: ");
        int idLieferer = scannInt(); // Eingabe einlesen
        System.out.println("Bitte geben Sie den Vornamen des Lieferers an: ");
        String vorname = SCANNER.nextLine(); // Eingabe einlesen

        SERVICE.insertLieferer(idLieferer, vorname); // Service aufrufen
    }

    private static void auslast() {
        SERVICE.getBezirke(); // Service aufrufen
        
        System.out.println("Bitte geben sie die Postleitzahl an: ");
            int plz = scannInt(); // Eingabe einlesen
        SERVICE.auslast(plz);// Service aufrufen
    }
    
    /**
     * Scannt Int
     * 
     * @return int input
     */
    private static int scannInt() {
        int input = 0;
        boolean korrekt = false;
        do
        {
            try {
                input = Integer.parseInt(SCANNER.nextLine());
                korrekt = true;
            } catch (NumberFormatException ex) {
                System.err.println(ex); 
                System.out.println("ERROR: Die Eingabe muss eine Zahl sein!");
            }
        } while (korrekt == false);
        return input;
    }
}