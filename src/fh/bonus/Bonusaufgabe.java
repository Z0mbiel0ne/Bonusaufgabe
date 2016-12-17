/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fh.bonus;

/**
 *
 * @author masch142
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Bonusaufgabe {
    
    private static String url;
    private static Connection conn;
    private static int anzahl;
    private static Scanner scanner = new Scanner(System.in);
    private static int selection;

    public static void main(String[] args) {
        
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            url="jdbc:mysql://127.0.0.1:3306/venenumbonus";
            conn = DriverManager.getConnection(url, "root", "");
            
        } 
        catch (Exception e) {
            System.out.println("*** Exception:\n" + e);
            e.printStackTrace();
        }
        
        do {
            
            System.out.println("============================");
            System.out.println("|        Verwaltung        |");
            System.out.println("============================");
            System.out.println("| Optionen:                |");
            System.out.println("|        1. Auslast        |");
            System.out.println("|        2. add Lieferer   |");
            System.out.println("|        3. change Bezirk  |");
            System.out.println("|        4. Exit           |");
            System.out.println("============================");
            
            selection = Integer.parseInt(scanner.nextLine());
            
            switch(selection)
            {
                case 1: 
                    System.out.println("Auslast anzeigen");
                    auslast();
                case 2:
                    System.out.println("Lieferer hinzufügen");
                    addLieferer();
                case 3: 
                    System.out.println("Bezirk ändern");
                    changeBezirk();
                case 4:
                    System.out.println("Exit");
                    break;
                default:
                    System.err.println("Bitte geben sie eine Gültige Zahl ein!");
                    break;
            }
        }  
        while(selection != 4);    
    }

    private static void changeBezirk() {
        System.out.println("Bitte geben sie die ID des Lieferers an: ");
         int id; 
          System.out.println("Bitte geben sie die Postleitzahl an: ");
            int input =  Integer.parseInt(scanner.nextLine());
         id = Integer.getInteger(scanner.nextLine());
         try {
            String sqlString = "UPDATE DBUSER SET USERNAME = ? WHERE USER_ID = ?";
            PreparedStatement stmt = conn.prepareStatement(sqlString);
            stmt.setInt(1, input);
            stmt.executeUpdate();
            stmt.close();
            
        } catch (Exception e) {
            System.out.println("*** Exception:\n" + e);
            e.printStackTrace();
        }
    }

    private static void addLieferer() {
     

        
    }

    private static void auslast() 
    {
          System.out.println("Bitte geben sie die Postleitzahl an: ");
            int input =  Integer.parseInt(scanner.nextLine());
         try {
            String sqlString = "SELECT COUNT(lieferer.idLieferer) as 'Lieferer', "
                    + "(Select count(bestellung.idBestellung) "
                    + "from bestellung join liefererbestaetigung ON bestellung.idBestellung = liefererbestaetigung.Bestellung_idBestellung "
                    + "where liefererbestaetigung.Lieferer_idLieferer = lieferer.idLieferer "
                    + "and bestellung.bestellstatus = 'abgeschlossen') as Bestellungen " 
                    + "FROM `lieferer_lieferbezirk` "                                                       //liefert Anzahl abgeschlossener Bestellungen und Lieferer für das Bezirk
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
            sqlString = "SELECT avg(anzahl*preis) " 
                    + "FROM bestellposition " 
                    + "JOIN artikel ON bestellposition.Artikel_idArtikel = artikel.idArtikel " 
                    + "JOIN bestellung ON bestellung.idBestellung = bestellposition.Bestellung_idBestellung " 
                    + "WHERE bestellstatus = 'abgeschlossen'";
            
            
        } catch (Exception e) {
            System.out.println("*** Exception:\n" + e);
            e.printStackTrace();
        }  
    }

    public Bonusaufgabe() {
        
    }
 }

