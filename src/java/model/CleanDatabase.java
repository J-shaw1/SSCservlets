package model;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Creates a clean database
 * @author joe
 */
public class CleanDatabase {
    
    private static Connection conn;
    
    /**
     * Creates deletes the old table and creates a new empty one
     * @param args 
     */
    public static void main(String[] args) {
	Connection conn = DatabaseUtils.getConnection();
	deleteOldTable();
	createNewTable();
    }

    private static void deleteOldTable() {
	try {
	    conn.prepareStatement("DROP TABLE Contacts");
	} catch (SQLException ex) {
	    System.err.println("Table could not be dropped");
	}
    }

    private static void createNewTable() {
	try {
	    conn.prepareStatement("CREATE TABLE Contacts ("
		    + "email VARCHAR(100)"
		    + "contactEmail VARCHAR(100)"
		    + "contactForename VARCHAR(30)"
		    + "contactSurname VARCHAR(30)"
		    + ");");
	} catch (SQLException ex) {
	    System.err.println("Could not create the table");
	}
    }
}
