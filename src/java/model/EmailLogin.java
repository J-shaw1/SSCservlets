package model;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Contains several methods for interacting with an email account
 *
 * @author Joe
 */
public class EmailLogin {

    String username;
    String password;
    Session session;

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
	session = Session.getDefaultInstance(prop);

	try {
	    //If we can get the store, then the login details must be valid
	    session.getStore("imaps").connect("imap.googlemail.com", username, password);
	    return true;
	} catch (MessagingException e) {
	    //Something went wrong, so the details must be invalid, or the connection failed
	    return false;
	}
    }

    /**
     * Sends an email with the login details that are already in the class
     *
     * @param to The main recipient
     * @param cc The email to CC, this gets decoded by this class, email's need
     * to be separated by spaces
     * @param subject The subject of the email
     * @param body The body of the email
     * @throws MessagingException If the email fails to send then this error is
     * thrown
     */
    public void sendEmail(String to, String cc, String subject, String body) throws MessagingException {

	String smtphost = "smtp.gmail.com";
	Properties prop = System.getProperties();
	prop.put("mail.smtp.auth", "true");
	prop.put("mail.smtp.starttls.enable", "true");
	prop.put("mail.smtp.host", smtphost);
	prop.put("mail.smtp.port", "587");

	Message message = new MimeMessage(session);

	//Set who the message is from and going to
	message.setFrom(new InternetAddress(username));

	message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

	String[] tos;
	if (cc != null && !cc.equals("")) {
	    tos = cc.split(" ");
	    for (String add : tos) {
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(add));
	    }
	}

	message.setSubject(subject);
	message.setText(body);

	//Send the email
	Transport trans = session.getTransport("smtp");
	trans.connect(smtphost, username, password);
	trans.sendMessage(message, message.getAllRecipients());

    }
}