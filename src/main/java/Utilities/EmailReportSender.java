package Utilities;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailReportSender {

    public static void sendEmail(String subject, String htmlContent) {
        // إعدادات السيرفر SMTP
        String host = "smtp.gmail.com"; // غيّرها حسب السيرفر اللي بتستخدمه
        String from = "mostafaesssam45@gmail.com";
        String password = "ldti lckt hqsb uayr"; // يفضل يطلع من config
        String to = "tatadarsh97@gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            // هنا نخلي الميل يبقى HTML body
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("✅ HTML Report Email Sent Successfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendReportByEmail(String reportPath) {
        // ايميل الإرسال (جيميل) + الباسورد (App Password اللي عملته)
        final String senderEmail = "mostafaesssam45@gmail.com";
        final String senderPassword = "ldti lckt hqsb uayr"; // App Password مش الباسورد العادي

        // ايميلات الاستقبال
        final String[] toEmails = {
                "tatadarsh97@gmail.com",
        };
        //final String ccEmail = "mostafamurad29@gmail.com";

        // إعدادات الـ SMTP الخاصة بجوجل
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

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

            // إضافة TO
            InternetAddress[] toAddresses = new InternetAddress[toEmails.length];
            for (int i = 0; i < toEmails.length; i++) {
                toAddresses[i] = new InternetAddress(toEmails[i]);
            }
            message.setRecipients(Message.RecipientType.TO, toAddresses);

            // إضافة CC
            //message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmail));

            message.setSubject("Automation Test Report");

            // نص الرسالة
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

            System.out.println("✅ Report sent successfully to TO and CC");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Failed to send email: " + e.getMessage());
        }
    }
}