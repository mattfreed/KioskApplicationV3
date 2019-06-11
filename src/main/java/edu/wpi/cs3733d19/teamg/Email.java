package edu.wpi.cs3733d19.teamg;

import edu.wpi.cs3733d19.teamg.models.Employee;
import edu.wpi.cs3733d19.teamg.models.ServiceRequest;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public final class Email {
    private static final String username = "goldgermans@gmail.com";
    private static final String password = "SoftEngTeamG!";
    private static final String emailValidation =
            "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";

    private static String textToHtml(String str) {
        String fin = "<main>";
        fin += str;
        fin = fin.replace("\uD83E\uDC78", "&larr;&emsp;");
        fin = fin.replace("\uD83E\uDC7A", "&rarr;&emsp;");
        fin = fin.replace("\u2BC5", "&uArr;&emsp;");
        fin = fin.replace("\u2BC6", "&dArr;&emsp;");
        fin = fin.replace("\uD83E\uDC79", "&uarr;&emsp;");
        fin = fin.replace("\u2B57", "&#9898;&emsp;");
        fin = fin.replace("\n", "<br>");
        fin += "<main>";
        return fin;
    }

    static boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile(emailValidation);
        Matcher mat = pattern.matcher(email);
        return mat.matches();
    }

    /**
     * Creates and sends an email with the given subject and body to the given addressee.
     *
     * @param subject   the email subject line
     * @param body   the email body
     * @param addressee the email address to which the email will be sent
     */
    public static void sendEmail(String subject, String body, String addressee) {
        Session session = generateSession(addressee);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("goldgermans@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(addressee));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

        } catch (MessagingException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Creates and sends an email with the given subject and body to the given addressee.
     *
     * @param subject   the email subject line
     * @param body   the email body
     * @param addressee the email address to which the email will be sent
     */
    public static void sendHtmlEmail(String subject, String body, String addressee) {
        Session session = generateSession(addressee);
        String newBody = textToHtml(body);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("goldgermans@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(addressee));
            message.setSubject(subject);
            message.setContent( newBody,
                    "text/html");

            Transport.send(message);

        } catch (MessagingException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static Session generateSession(String addressee) {
        if (!validateEmail(addressee)) {
            return null;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        return Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    /**
     * Generates an email message to be sent when a ServiceRequest is created or assigned.
     *
     * @param employee the employee to send the email to
     * @param request  the request in question
     * @param message  whether the employee created the request or was assigned to it
     * @return the generated message
     */
    public static String generateServiceRequestMessage(Employee employee,
                                                       ServiceRequest request, String message) {
        String fin = "Hi, ";
        fin += employee.getFirstName();
        fin += " ";
        fin += employee.getLastName();
        fin += "!\n\n";
        fin += message;
        String requestType = request.getCategory().getTitle();
        char firstLetter = requestType.toCharArray()[0];
        if (firstLetter == 'a' || firstLetter == 'e' || firstLetter == 'i'
                || firstLetter == 'o' || firstLetter == 'u') {
            fin += "an ";
        } else {
            fin += "a ";
        }
        fin += requestType;
        fin += " request, at [";
        fin += request.getLocation().getLongName();
        fin += "].\n\nHave yourself a super splendiferous day!";

        return fin;
    }
}