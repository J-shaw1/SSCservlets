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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

/**
 *
 * @author joe
 */
public class DatabaseUtils {

    public static Connection getConnection() {
	Connection conn = null;

	try {
	    Class.forName("org.postgresql.Driver");
	} catch (ClassNotFoundException ex) {
	    throw new RuntimeException("Driver not found");
	}

	try {
	    conn = DriverManager.getConnection("jdbc:postgresql://dbteach2/jgs328", "jgs328", "qwerty1234");
	} catch (SQLException ex) {
	    throw new RuntimeException("Failed to connect to the database");
	}

	return conn;
    }

    public static ArrayList<ContactDetail> searchContact(String forename, String surname, HttpSession session) {

	String username = (String) session.getAttribute("username");

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
	    throw new RuntimeException("Database query failed");
	}
    }

    private static ResultSet searchSur(String username, String surname) throws SQLException {

	PreparedStatement p = getConnection().prepareStatement(
		"SELECT contactForename, contactSurname, contactEmail"
		+ "FROM Contacts"
		+ "WHERE email == ? && contactSurname == ?");
	p.setString(1, username);
	p.setString(2, surname);

	return p.executeQuery();
    }

    private static ResultSet searchFore(String username, String forename) throws SQLException {
	PreparedStatement p = getConnection().prepareStatement(
		"SELECT contactForename, contactSurname, contactEmail"
		+ "FROM Contacts"
		+ "WHERE email == ? && contactForename == ?");
	p.setString(1, username);
	p.setString(2, forename);

	return p.executeQuery();
    }

    private static ResultSet searchBoth(String username, String forename, String surname) throws SQLException {
	PreparedStatement p = getConnection().prepareStatement(
		"SELECT contactForename, contactSurname, contactEmail"
		+ "FROM Contacts"
		+ "WHERE email == ? && contactForename == ? && contactSurname == ?");
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
}
