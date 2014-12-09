package model;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Creates a clean database, this is never called by a servlet
 *
 * @author joe
 */
public final class CleanDatabase {

    /**
     * Creates deletes the old table and creates a new empty one
     *
     * @param args
     */
    public static void main(String[] args) {
        Connection conn = DatabaseUtils.getConnection();
        deleteOldTable(conn);
        createNewTable(conn);
    }

    /**
     * Drops the old contacts table
     *
     * @param conn The connection to the database
     */
    private static void deleteOldTable(Connection conn) {
        try {
            //Drop the old table
            conn.prepareStatement("DROP TABLE Contacts").execute();
        } catch (SQLException ex) {
            System.err.println("Table could not be dropped");
            ex.printStackTrace();
        }
    }

    /**
     * Creates the new table
     *
     * @param conn The connection to the database
     */
    private static void createNewTable(Connection conn) {
        try {
            //Create the new table
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
