package com.jannetta.certify.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;

public class EmailUtil {
    static Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    /**
     * Utility method to send simple HTML email
     *
     * @param session
     * @param toEmail
     * @param subject
     * @param body
     */
    public static int sendEmail(Session session, String toEmail, String subject, String body, String fromEmail,
                                String replyToEmail, String user_id) {
        try {
            String filename = Globals.getProperty("directory.pdf").concat("/").concat(user_id) + ".pdf";
            Message msg = new MimeMessage(session);
            BodyPart messageBodyPart = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();
            DataSource source = new FileDataSource(filename);

            msg.setFrom(new InternetAddress(fromEmail));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            msg.setSubject(subject);

            messageBodyPart.setText(body);
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);
            msg.setContent(multipart);

            logger.debug("Message is ready");
            Transport.send(msg);
            logger.debug("Message sent");
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }
}

