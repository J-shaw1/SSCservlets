package model;

/**
 * Holds contact details for a user
 *
 * @author joe
 */
public class ContactDetail {

    String email;
    String f;
    String s;

    /**
     * Creates the contact object
     *
     * @param email The email to hold
     * @param f The contacts first name;
     * @param s THe contacts second name
     */
    public ContactDetail(String f, String s, String email) {
        this.email = email;
        this.f = f;
        this.s = s;
    }

    /**
     * Gets the email for this contact
     *
     * @return The email
     */
    public String getEmail() {
        if (email != null) {
            return email;
        } else {
            return "";
        }
    }

    /**
     * Get the contacts forename
     *
     * @return The forename
     */
    public String getForename() {
        if (f != null) {
            return f;
        } else {
            return "";
        }
    }

    /**
     * Gets the surname of the contact
     *
     * @return The surname
     */
    public String getSurname() {
        if (s != null) {
            return s;
        } else {
            return "";
        }
    }
}
