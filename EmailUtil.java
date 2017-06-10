package application;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {
	/**
	 * The administrator email address
	 * */
	private static String fromEmail = ""; //requires valid gmail id
	/**The administrator email profile name*/
	private static String fromName="Media Center";
	/**The administrator email password*/
	private static String password = ""; // correct password for gmail id
	/**
	 * Changes the administrator email address and password
	 * @param from The administrator email address
	 * @param pass The administrator email password
	 */
	public static void setFrom(String from, String pass){
		fromEmail=from;
		password=pass;
	}
	/**
	 * Sends an email given a recipient, a subject, and a body
	 * @param toEmail The recipient of the email
	 * @param subject The subject of the email
	 * @param body The content of the email
	 * @throws MessagingException If email sending fails
	 * @throws UnsupportedEncodingException If email sending fails
	 */
	public static void sendEmail(String toEmail, String subject, String body) throws MessagingException, UnsupportedEncodingException{
		if(toEmail.trim().equals("")){throw new MessagingException();}
		System.out.println("TLSEmail Start");
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
		props.put("mail.smtp.port", "587"); //TLS Port
		props.put("mail.smtp.auth", "true"); //enable authentication
		props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

		//create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		Session session = Session.getInstance(props, auth);

		MimeMessage msg = new MimeMessage(session);
		//set message headers
		msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
		msg.addHeader("format", "flowed");
		msg.addHeader("Content-Transfer-Encoding", "8bit");

		msg.setFrom(new InternetAddress(fromEmail, fromName));
		msg.setReplyTo(InternetAddress.parse(fromEmail, false));

		msg.setSubject(subject, "UTF-8");

		msg.setText(body, "UTF-8");

		msg.setSentDate(new Date());
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
		System.out.println("Message is ready");
		Transport.send(msg);  

		System.out.println("Email Sent Successfully!!");

	}
}