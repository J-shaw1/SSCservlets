package model;

import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;

/**
 * Contains several methods for interacting with an email account
 *
 * @author Joe
 */
public class EmailLogin {

    String username;
    String password;
    Session s;

    /**
     * Create the object with the username and password
     *
     * @param username The username to use in the object
     * @param password The password to use in the object
     */
    public EmailLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Tests whether the given login details are valid for accessing and email
     * account
     *
     * @return True if the details are valid, false if they are not valid.
     */
    public boolean testLoginDetails() {

        //Set all properties needed
        Properties prop = System.getProperties();
        prop.setProperty("mail.store.protocol", "imaps");
        prop.setProperty("mail.user", username);
        prop.setProperty("mail.password", password);

        //Create a session
        s = Session.getDefaultInstance(prop);

        try {
            //If we can get the store, then the login details must be valid
            s.getStore("imaps").connect("imap.googlemail.com", username, password);
            return true;
        } catch (MessagingException e) {
            //Something went wrong, so the details must be invalid, or the connection failed
            return false;
        }
    }
}
