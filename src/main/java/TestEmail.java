
import com.jannetta.certify.controller.Globals;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import com.jannetta.certify.controller.Globals;

public class TestEmail {
    static Globals globals = Globals.getInstance();
    public static void main(String[] args) {
        // Recipient's email ID needs to be mentioned.
        String to = "jannetta.steyn@newcastle.ac.uk";

        // Sender's email ID needs to be mentioned
        String from = globals.getProperty("mail.from");

        final String username = globals.getProperty("mail.from");//change accordingly
        final String password = globals.getProperty("mail.password");//change accordingly

        // Get the Session object.
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };
        Session session = Session.getInstance(Globals.getProperties(), auth);
        session.setDebug(true);

        try {
            Globals.getProperties().forEach((prop, value) -> {
                System.out.println(prop + ": " + value);
            });

            // Part two is attachment
            String filename = Globals.getProperty("directory.pdf") + "/Ben_Dixon.pdf";
            DataSource source = new FileDataSource(filename);
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);
            // Create a multipart message
            Multipart multipart = new MimeMultipart();
            // Create the message part
            BodyPart bodyPart = new MimeBodyPart();

            // Now set the actual message
            bodyPart.setText("This is message body");
            bodyPart.setDataHandler(new DataHandler(source));

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from, "NoREply"));
            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            // Set Subject: header field
            message.setSubject("Testing Subject");
            message.addHeader("Content-type", "text/HTML; charset=UTF-8");
            message.addHeader("format", "flowed");
            message.addHeader("Content-Transfer-Encoding", "8bit");
            message.setSentDate(new Date());

            bodyPart.setFileName(filename);
            multipart.addBodyPart(bodyPart);
            message.setContent(multipart);

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}