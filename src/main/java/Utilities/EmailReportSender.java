package Utilities;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailReportSender {

    public static void sendEmail(String subject, String htmlContent) {
        // إعدادات السيرفر SMTP
        String host = "mail.privateemail.com";
        String from = "alerts@shofha.net";
        String password = "LM5nH2dTLS";

        // ايميلات الاستقبال (ممكن تزود أي عدد)
        String[] toEmails = {
//                "tatadarsh97@gmail.com",
                "mustafa.essam@shofha.com",
                "mohamed.abouelnaga@shofha.com",
                "mohamed.osama@arpuplus.com",
                "galal.elrihany@shofha.com"
       };

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.ssl.enable", "true");   // ✅ استخدام SSL
        props.put("mail.smtp.starttls.enable", "false"); // ✅ مش هنستخدم STARTTLS مع 465

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));

            // إضافة Recipients (أكتر من TO)
            InternetAddress[] toAddresses = new InternetAddress[toEmails.length];
            for (int i = 0; i < toEmails.length; i++) {
                toAddresses[i] = new InternetAddress(toEmails[i]);
            }
            message.setRecipients(Message.RecipientType.TO, toAddresses);

            message.setSubject(subject);

            // هنا نخلي الميل يبقى HTML body
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("✅ HTML Report Email Sent Successfully to multiple recipients!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendReportByEmail(String reportPath) {
        // ايميل الإرسال (المؤسسي) + الباسورد
        final String senderEmail = "alerts@shofha.net";
        final String senderPassword = "LM5nH2dTLS";

        // ايميلات الاستقبال (ممكن تزود أي عدد)
        final String[] toEmails = {
                "tatadarsh97@gmail.com",
                "mostafamurad29@gmail.com"
        };
        // final String[] ccEmails = { "manager@shofha.net" }; // ممكن تستخدم CC

        // إعدادات SMTP الخاصة بـ privateemail
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", "mail.privateemail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true"); // ✅ استخدام SSL
        properties.put("mail.smtp.starttls.enable", "false"); // ✅ مش هنحتاج STARTTLS مع 465

        // عمل Session مع المصادقة
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // بناء الرسالة
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));

            // إضافة TO (أكتر من شخص)
            InternetAddress[] toAddresses = new InternetAddress[toEmails.length];
            for (int i = 0; i < toEmails.length; i++) {
                toAddresses[i] = new InternetAddress(toEmails[i]);
            }
            message.setRecipients(Message.RecipientType.TO, toAddresses);

            // إضافة CC لو عايز
            /*
            InternetAddress[] ccAddresses = new InternetAddress[ccEmails.length];
            for (int i = 0; i < ccEmails.length; i++) {
                ccAddresses[i] = new InternetAddress(ccEmails[i]);
            }
            message.setRecipients(Message.RecipientType.CC, ccAddresses);
            */

            message.setSubject("Automation Test Report");

            // نص الميل
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Hello Team,\n\nPlease find attached the latest Automation Test Report.\n\nBest regards,\nTest Automation System");

            // مرفق (التقرير)
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(reportPath);

            // تجميع النص + المرفق
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            // إرسال الإيميل
            Transport.send(message);

            System.out.println("✅ Report sent successfully to multiple recipients from alerts@shofha.net");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Failed to send email: " + e.getMessage());
        }
    }
}