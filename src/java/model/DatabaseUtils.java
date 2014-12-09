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
     *
     * @return The connection to the database
     */
    public static Connection getConnection() {
        Connection conn = null;

        //String for connecting to the database
        final String SERVER_ADDRESS = "jdbc:postgresql://localhost/Con";
        final String SERVER_USERNAME = "postgres";
        final String SERVER_PASSWORD = "password";

        try {
            //See if the class for the driver exists
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Driver not found");
        }

        try {
            //Try to get the connection
            conn = DriverManager.getConnection(SERVER_ADDRESS, SERVER_USERNAME, SERVER_PASSWORD);
            System.err.println("Connection made");
        } catch (SQLException ex) {
            System.err.println("Connection failed");
            ex.printStackTrace();
            throw new RuntimeException("Failed to connect to the database");
        }
        return conn;
    }

    /**
     * Tests if a contact exists
     *
     * @param forename The forename to search for
     * @param surname The surname to search for
     * @param session The current session, used to get the current logged in
     * user
     * @return An ArrayList of contactDetails, empty if there are no results
     */
    public static ArrayList<ContactDetail> searchContact(String forename, String surname, HttpSession session) {

        String username = (String) session.getAttribute("username");
        System.err.println(username);
        try {
            if (forename == null || (forename.equals("")) && (surname == null || surname.equals(""))) {
                //If they are both empty then throw an error, TODO redirect back to menu page
                throw new RuntimeException("The two search fields were empty");
            } else if (forename == null || forename.equals("")) {
                //Case that forename is empty then search for surname
                return processResults(searchSur(username, surname));
            } else if (surname == null || surname.equals("")) {
                //If the surname is empty then search for forename
                return processResults(searchFore(username, forename));
            } else {
                //neither and empty, then search for both
                return processResults(searchBoth(username, forename, surname));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Database query failed");
        }
    }

    /**
     * A search for just a surname
     *
     * @param username The logged in username
     * @param surname The surname to search for
     * @return The set of results that were found with that surname
     * @throws SQLException If the search cannot be completed.
     */
    private static ResultSet searchSur(String username, String surname) throws SQLException {

        PreparedStatement p = getConnection().prepareStatement(
                "SELECT c.contactForename, c.contactSurname, c.contactEmail "
                + "FROM Contacts c "
                + "WHERE c.email = ? AND c.contactSurname = ?;");
        p.setString(1, username);
        p.setString(2, surname);

        return p.executeQuery();
    }

    /**
     * A search for just a forename
     *
     * @param username The username of the current logged in user
     * @param forename The forename to search for
     * @return The set of results that were found when searching the forename
     * @throws SQLException Thrown if the search cannot be completed
     */
    private static ResultSet searchFore(String username, String forename) throws SQLException {
        PreparedStatement p = getConnection().prepareStatement(
                "SELECT c.contactForename, c.contactSurname, c.contactEmail "
                + "FROM Contacts c "
                + "WHERE c.email = ? AND c.contactForename = ?;");
        p.setString(1, username);
        p.setString(2, forename);

        return p.executeQuery();
    }

    /**
     * Searches for both a forename and a surname
     *
     * @param username The username of the current logged in user
     * @param forename The forename to search for
     * @param surname The surname to search for
     * @return The set of results found when searching for the forename and
     * surname
     * @throws SQLException Thrown if the search could not be completed
     */
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

    /**
     * Turns the ResultSet into an ArrayList of ContactDetails to name it easier
     * to precess later
     *
     * @param results The ResultSet
     * @return The ArrayList of ContactDetails
     */
    private static ArrayList<ContactDetail> processResults(ResultSet results) {

        ArrayList<ContactDetail> contacts = new ArrayList<>();

        try {
            //For every result in the result set create a ContactDetail and add it to the List
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

    /**
     * Add a contact to the database
     *
     * @param session The current session, used for getting the current logged
     * in user
     * @param forename The forename of the contact to add
     * @param surname The surname of the contact to add
     * @param email The email of the contact to add
     * @throws SQLException Thrown if the contact cannot be added
     */
    public static void addContact(HttpSession session, String forename, String surname, String email) throws SQLException {
        PreparedStatement p = getConnection().prepareStatement(
                "INSERT INTO Contacts VALUES (?,?,?,?)");
        //Set all the string in the insert statement
        p.setString(1, (String) session.getAttribute("username"));
        p.setString(2, email);
        p.setString(3, forename);
        p.setString(4, surname);
        p.execute();
    }

}
