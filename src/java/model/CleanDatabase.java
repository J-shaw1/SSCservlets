package model;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Creates a clean database
 * @author joe
 */
public class CleanDatabase {
    
    /**
     * Creates deletes the old table and creates a new empty one
     * @param args 
     */
    public static void main(String[] args) {
	Connection conn = DatabaseUtils.getConnection();
	deleteOldTable(conn);
	createNewTable(conn);
    }

    private static void deleteOldTable(Connection conn) {
	try {
	    conn.prepareStatement("DROP TABLE Contacts").execute();
	} catch (SQLException ex) {
	    System.err.println("Table could not be dropped");
            ex.printStackTrace();
	}
    }

    private static void createNewTable(Connection conn) {
	try {
	    conn.prepareStatement("CREATE TABLE Contacts ("
		    + "email VARCHAR(100),"
		    + "contactEmail VARCHAR(100),"
		    + "contactForename VARCHAR(30),"
		    + "contactSurname VARCHAR(30)"
		    + ");").execute();
	} catch (SQLException ex) {
	    System.err.println("Could not create the table");
            ex.printStackTrace();
	}
    }
}
