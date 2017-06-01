package application;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailUtil {
	static String fromEmail = "smcs2019.ssmg@gmail.com"; //requires valid gmail id
	static String fromName="Media Center";
	static String password = "Zahoogle13"; // correct password for gmail id

	public static void setFrom(String from, String pass) throws UnsupportedEncodingException, MessagingException{
		fromEmail=from;
		password=pass;
	}
	public static void sendEmail(String toEmail, String subject, String body, File f) throws MessagingException, UnsupportedEncodingException{
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
		if(f!=null){
			MimeBodyPart filePart=new MimeBodyPart();
			Multipart m=new MimeMultipart();
			filePart.setDataHandler(new DataHandler(new FileDataSource(f)));
			m.addBodyPart(filePart);
			msg.setContent(m);
		}
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
		System.out.println("Message is ready");
		Transport.send(msg);  

		System.out.println("Email Sent Successfully!!");

	}
}