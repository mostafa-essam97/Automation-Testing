package Utilities;

import config.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Properties;

/**
 * EmailReportSender - Sends test reports via email
 * Uses configuration from config.properties and environment variables
 */
public class EmailReportSender {
    private static final Logger logger = LoggerFactory.getLogger(EmailReportSender.class);

    /**
     * Send HTML report as email body (not attachment)
     */
    public static void sendHtmlReportEmail(String reportPath) {
        logger.info("========================================");
        logger.info("Sending HTML Report Email...");
        logger.info("========================================");

        String host = ConfigReader.getEmailSmtpHost();
        String from = ConfigReader.getEmailFrom();
        String password = ConfigReader.getEmailPassword();
        String[] toEmails = ConfigReader.getEmailRecipients();

        // Log configuration
        logger.info("Email Configuration:");
        logger.info("   SMTP Host: {}", host);
        logger.info("   From: {}", from != null ? from : "NOT SET");
        logger.info("   Password: {}", password != null ? "****" : "NOT SET");
        logger.info("   Recipients: {}", Arrays.toString(toEmails));

        // Validate credentials
        if (from == null || from.isEmpty()) {
            logger.error("❌ EMAIL_FROM not configured!");
            logger.error("   Set environment variable EMAIL_FROM or update config.properties");
            return;
        }

        if (password == null || password.isEmpty()) {
            logger.error("❌ EMAIL_PASSWORD not configured!");
            logger.error("   Set environment variable EMAIL_PASSWORD or update config.properties");
            return;
        }

        if (toEmails == null || toEmails.length == 0 || toEmails[0].isEmpty()) {
            logger.error("❌ No email recipients configured!");
            return;
        }

        // Read HTML report content
        String htmlContent;
        try {
            File reportFile = new File(reportPath);
            if (!reportFile.exists()) {
                logger.error("❌ Report file not found: {}", reportPath);
                return;
            }
            htmlContent = new String(Files.readAllBytes(reportFile.toPath()));
            logger.info("Report file read successfully: {} bytes", htmlContent.length());
        } catch (Exception e) {
            logger.error("❌ Failed to read report file: {}", e.getMessage());
            return;
        }

        // Email properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", host);
        props.put("mail.debug", "true"); // Enable debug for troubleshooting

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));

            // Add recipients
            InternetAddress[] toAddresses = new InternetAddress[toEmails.length];
            for (int i = 0; i < toEmails.length; i++) {
                toAddresses[i] = new InternetAddress(toEmails[i].trim());
                logger.info("   Adding recipient: {}", toEmails[i].trim());
            }
            message.setRecipients(Message.RecipientType.TO, toAddresses);

            // Subject with timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            message.setSubject("🧪 Shofha Automation Test Report - " + timestamp);

            // Set HTML content as body
            message.setContent(htmlContent, "text/html; charset=utf-8");

            // Send
            logger.info("Sending email...");
            Transport.send(message);

            logger.info("✅ HTML Report Email sent successfully!");
            logger.info("   To: {}", Arrays.toString(toEmails));

        } catch (MessagingException e) {
            logger.error("❌ Failed to send email: {}", e.getMessage());
            logger.error("   Exception: ", e);
        }
    }

    /**
     * Send report with attachment
     */
    public static void sendReportByEmail(String reportPath) {
        logger.info("========================================");
        logger.info("Sending Report Email with Attachment...");
        logger.info("========================================");

        String senderEmail = ConfigReader.getEmailFrom();
        String senderPassword = ConfigReader.getEmailPassword();
        String[] toEmails = ConfigReader.getEmailRecipients();
        String host = ConfigReader.getEmailSmtpHost();

        // Log configuration
        logger.info("Email Configuration:");
        logger.info("   SMTP Host: {}", host);
        logger.info("   From: {}", senderEmail != null ? senderEmail : "NOT SET");
        logger.info("   Password: {}", senderPassword != null ? "****" : "NOT SET");
        logger.info("   Recipients: {}", Arrays.toString(toEmails));
        logger.info("   Report Path: {}", reportPath);

        // Validate credentials
        if (senderEmail == null || senderEmail.isEmpty()) {
            logger.error("❌ EMAIL_FROM not configured!");
            logger.error("   Set environment variable EMAIL_FROM");
            logger.error("   Example: set EMAIL_FROM=automation@shofha.com");
            logger.info("📁 Report saved locally at: {}", reportPath);
            return;
        }

        if (senderPassword == null || senderPassword.isEmpty()) {
            logger.error("❌ EMAIL_PASSWORD not configured!");
            logger.error("   Set environment variable EMAIL_PASSWORD");
            logger.info("📁 Report saved locally at: {}", reportPath);
            return;
        }

        if (toEmails == null || toEmails.length == 0 || toEmails[0].isEmpty()) {
            logger.error("❌ No email recipients configured!");
            logger.info("📁 Report saved locally at: {}", reportPath);
            return;
        }

        // Validate report file exists
        File reportFile = new File(reportPath);
        if (!reportFile.exists()) {
            logger.error("❌ Report file not found: {}", reportPath);
            logger.error("   Working directory: {}", System.getProperty("user.dir"));
            return;
        }
        logger.info("Report file found: {} ({} KB)", reportPath, reportFile.length() / 1024);

        // Email properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.trust", host);
        properties.put("mail.debug", "false");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));

            // Add TO recipients
            InternetAddress[] toAddresses = new InternetAddress[toEmails.length];
            for (int i = 0; i < toEmails.length; i++) {
                String email = toEmails[i].trim();
                toAddresses[i] = new InternetAddress(email);
                logger.info("   Adding recipient: {}", email);
            }
            message.setRecipients(Message.RecipientType.TO, toAddresses);

            // Subject with timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            message.setSubject("🧪 Shofha Automation Test Report - " + timestamp);

            // Email body (HTML)
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            String htmlBody = buildEmailBody();
            messageBodyPart.setContent(htmlBody, "text/html; charset=utf-8");

            // Attachment
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(reportPath);
            attachmentPart.setFileName("TestReport_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")) + ".html");

            // Combine body + attachment
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            // Send
            logger.info("Sending email...");
            Transport.send(message);

            logger.info("========================================");
            logger.info("✅ Report Email sent successfully!");
            logger.info("   From: {}", senderEmail);
            logger.info("   To: {}", Arrays.toString(toEmails));
            logger.info("   Attachment: {}", reportFile.getName());
            logger.info("========================================");

        } catch (AuthenticationFailedException authEx) {
            logger.error("❌ Email authentication failed!");
            logger.error("   Check your EMAIL_FROM and EMAIL_PASSWORD");
            logger.error("   Error: {}", authEx.getMessage());
        } catch (Exception e) {
            logger.error("❌ Failed to send email: {}", e.getMessage());
            logger.error("   Stack trace:", e);
        }
    }

    /**
     * Build HTML email body
     */
    private static String buildEmailBody() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head><meta charset='UTF-8'></head>" +
                "<body style='font-family: Arial, sans-serif; padding: 20px;'>" +
                "<h2 style='color: #2c3e50;'>🧪 Shofha Automation Test Report</h2>" +
                "<hr style='border: 1px solid #3498db;'>" +
                "<p>Hello Team,</p>" +
                "<p>Please find attached the latest <b>Automation Test Report</b> for Shofha subscription flow.</p>" +
                "<p><b>Report Generated:</b> " + timestamp + "</p>" +
                "<hr style='border: 1px solid #ecf0f1;'>" +
                "<p style='color: #7f8c8d; font-size: 12px;'>Best regards,<br>Test Automation System<br>Shofha - Arpu Square</p>" +
                "</body>" +
                "</html>";
    }

    /**
     * Send simple text email (for testing)
     */
    public static void sendTestEmail() {
        logger.info("Sending test email...");
        
        String from = ConfigReader.getEmailFrom();
        String password = ConfigReader.getEmailPassword();
        String[] toEmails = ConfigReader.getEmailRecipients();
        String host = ConfigReader.getEmailSmtpHost();

        if (from == null || password == null) {
            logger.error("❌ Email credentials not set!");
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            
            for (String email : toEmails) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.trim()));
            }
            
            message.setSubject("Test Email - Shofha Automation");
            message.setText("This is a test email from Shofha Automation System.\n\nIf you received this, email is working correctly!");

            Transport.send(message);
            logger.info("✅ Test email sent successfully!");

        } catch (Exception e) {
            logger.error("❌ Failed to send test email: {}", e.getMessage());
        }
    }
}
