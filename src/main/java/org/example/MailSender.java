package org.example;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSender {

    private MailSender() {
    }

    public static void send(String login, String password, String to, String subject, String message) {

        // Microsoft SMTP server address
        String host = "smtp.office365.com";

        // Set properties for the SMTP server
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        // Get the Session object and pass the username and password
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(login, password);
            }
        });

        try {
            // Create a default MimeMessage object
            Message mimeMessage = new MimeMessage(session);

            // Set From
            mimeMessage.setFrom(new InternetAddress(login));

            // Set To
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            // Set Subject
            mimeMessage.setSubject(subject);

            // Set the body of the email
            mimeMessage.setText(message);

            // Send message
            Transport.send(mimeMessage);

            System.out.println("Email has been sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
