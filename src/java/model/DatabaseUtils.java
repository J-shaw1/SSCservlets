/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;

/**
 * Contains several methods for contact database interaction
 *
 * @author joe
 */
public class DatabaseUtils {

    /**
     * Gets a new connection to the database
     * @return The connection to the database
     */
    public static Connection getConnection() {
	Connection conn = null;

	final String SERVER_ADDRESS = "jdbc:postgresql://localhost/Con";
	final String SERVER_USERNAME = "postgres";
	final String SERVER_PASSWORD = "password";
	
	try {
	    Class.forName("org.postgresql.Driver");
	} catch (ClassNotFoundException ex) {
	    throw new RuntimeException("Driver not found");
	}

	try {
	    conn = DriverManager.getConnection(SERVER_ADDRESS, SERVER_USERNAME, SERVER_PASSWORD);
            System.err.println("Connection made");
	} catch (SQLException ex) {
            System.err.println("Connection failed");
	    ex.printStackTrace();
	    throw new RuntimeException("Failed to connect to the database");
	}
	return conn;
    }

    public static ArrayList<ContactDetail> searchContact(String forename, String surname, HttpSession session) {

	String username = (String) session.getAttribute("username");
        System.err.println(username);
	try {
	    if (forename == null || (forename.equals("")) && (surname == null || surname.equals(""))) {
		throw new RuntimeException("The two search fields were empty");
	    } else if (forename == null || forename.equals("")) {
		return processResults(searchSur(username, surname));
	    } else if (surname == null || surname.equals("")) {
		return processResults(searchFore(username, forename));
	    } else {
		return processResults(searchBoth(username, forename, surname));
	    }
	} catch (SQLException ex) {
            ex.printStackTrace();
	    throw new RuntimeException("Database query failed");
	}
    }

    private static ResultSet searchSur(String username, String surname) throws SQLException {

	PreparedStatement p = getConnection().prepareStatement(
		"SELECT c.contactForename, c.contactSurname, c.contactEmail "
		+ "FROM Contacts c "
		+ "WHERE c.email = ? AND c.contactSurname = ?;");
	p.setString(1, username);
	p.setString(2, surname);

	return p.executeQuery();
    }

    private static ResultSet searchFore(String username, String forename) throws SQLException {
	PreparedStatement p = getConnection().prepareStatement(
		"SELECT c.contactForename, c.contactSurname, c.contactEmail "
		+ "FROM Contacts c "
		+ "WHERE c.email = ? AND c.contactForename = ?;");
	p.setString(1, username);
	p.setString(2, forename);

	return p.executeQuery();
    }

    private static ResultSet searchBoth(String username, String forename, String surname) throws SQLException {
	PreparedStatement p = getConnection().prepareStatement(
		"SELECT c.contactForename, c.contactSurname, c.contactEmail "
		+ "FROM Contacts c "
		+ "WHERE ((c.email = ?) AND (c.contactForename = ?) AND (c.contactSurname = ?));");
        
        System.out.println(username);
	p.setString(1, username);
	p.setString(2, forename);
	p.setString(3, surname);

	return p.executeQuery();
    }

    private static ArrayList<ContactDetail> processResults(ResultSet results) {

	ArrayList<ContactDetail> contacts = new ArrayList<>();

	try {
	    while (results.next()) {
		contacts.add(new ContactDetail(results.getString(1),
			results.getString(2),
			results.getString(3)));
	    }
	} catch (SQLException ex) {
	    throw new RuntimeException("Error when processing results");
	}

	return contacts;
    }
    
    public static void addContact(HttpSession session, String forename, String surname, String email) throws SQLException{
	PreparedStatement p = getConnection().prepareStatement(
		"INSERT INTO Contacts VALUES (?,?,?,?)");
	p.setString(1, (String) session.getAttribute("username"));
	p.setString(2, email);
	p.setString(3, forename);
	p.setString(4, surname);
        p.execute();
    }
    
}
