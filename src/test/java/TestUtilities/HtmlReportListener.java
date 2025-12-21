package TestUtilities;

import Utilities.EmailReportSender;
import Utilities.HtmlReportBuilder;
import Utilities.ReportDataModel;
import Utilities.TestContext;
import config.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * HtmlReportListener - Collects test data and sends HTML email report
 */
public class HtmlReportListener implements ITestListener {
    private static final Logger logger = LoggerFactory.getLogger(HtmlReportListener.class);
    
    // Shared data across all tests in the suite
    private static List<ReportDataModel> reportDataList = new ArrayList<>();
    
    // Cached test data (updated after each test)
    private static String cachedReservedNumber = "";
    private static String cachedFullNumber = "";
    private static String cachedCountryCode = "";
    private static String cachedPackageType = "";
    private static String cachedPackagePrice = "";
    private static String cachedSubscriptionTime = "";
    private static String cachedOtpCode = "";

    @Override
    public void onTestStart(ITestResult result) {
        logger.debug("Test started: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("✅ Test PASSED: {}", result.getMethod().getMethodName());
        updateCachedData();
        addReportData(result, "PASS");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("❌ Test FAILED: {}", result.getMethod().getMethodName());
        updateCachedData();
        addReportData(result, "FAIL");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("⏭️ Test SKIPPED: {}", result.getMethod().getMethodName());
        addReportData(result, "SKIPPED");
    }

    @Override
    public void onStart(ITestContext context) {
        logger.info("========================================");
        logger.info("HTML Report Listener Started");
        logger.info("========================================");
        // Clear previous data
        reportDataList.clear();
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("========================================");
        logger.info("Test Suite Finished - Sending Email Report");
        logger.info("========================================");
        
        // Build HTML content
        String htmlContent = HtmlReportBuilder.build(reportDataList);
        
        // Log report summary
        logger.info("Report Summary:");
        logger.info("   Total Tests: {}", reportDataList.size());
        long passed = reportDataList.stream().filter(r -> "PASS".equals(r.getStatus())).count();
        long failed = reportDataList.stream().filter(r -> "FAIL".equals(r.getStatus())).count();
        logger.info("   Passed: {}", passed);
        logger.info("   Failed: {}", failed);
        
        // Send email
        sendHtmlEmail(htmlContent);
    }

    /**
     * Update cached data from TestContext
     */
    private void updateCachedData() {
        try {
            TestContext.TestData data = TestContext.getData();
            
            if (data.getReservedNumberText() != null) {
                cachedReservedNumber = data.getReservedNumberText();
            }
            if (data.getFullReservedNumber() != null) {
                cachedFullNumber = data.getFullReservedNumber();
            }
            if (data.getCountryCode() != null) {
                cachedCountryCode = data.getCountryCode();
            }
            if (data.getPackageType() != null) {
                cachedPackageType = data.getPackageType();
            }
            if (data.getPackagePrice() != null) {
                cachedPackagePrice = data.getPackagePrice();
            }
            if (data.getSubscriptionTimestamp() != null) {
                cachedSubscriptionTime = data.getSubscriptionTimestamp();
            }
            if (data.getOtpCode() != null) {
                cachedOtpCode = data.getOtpCode();
            }
        } catch (Exception e) {
            logger.debug("Could not update cached data: {}", e.getMessage());
        }
    }

    /**
     * Add test result to report data list
     */
    private void addReportData(ITestResult result, String status) {
        ReportDataModel model = new ReportDataModel();

        // Execution Data
        model.setTestCaseName(result.getMethod().getMethodName());
        model.setClassName(result.getTestClass().getName());
        model.setStatus(status);
        model.setDuration((result.getEndMillis() - result.getStartMillis()) / 1000 + " s");

        // Business Data from cached values
        model.setReservedNumberText(formatReservedNumber());
        model.setFullReservedNumber(cachedFullNumber);
        model.setCountryCode(cachedCountryCode);
        model.setPackageType(cachedPackageType);
        model.setPackagePrice(cachedPackagePrice);
        model.setSubscriptionTimestamp(cachedSubscriptionTime);
        model.setOtpCode(cachedOtpCode);

        // Generate comment based on status
        model.setComment(generateComment(result, status));

        reportDataList.add(model);
        
        logger.debug("Added report data: {} - {}", model.getTestCaseName(), status);
    }

    /**
     * Format reserved number for display
     */
    private String formatReservedNumber() {
        if (cachedCountryCode != null && !cachedCountryCode.isEmpty() && 
            cachedFullNumber != null && !cachedFullNumber.isEmpty()) {
            return "AE e&\n" + cachedFullNumber;
        }
        return cachedReservedNumber;
    }

    /**
     * Generate comment based on test result
     */
    private String generateComment(ITestResult result, String status) {
        if ("PASS".equals(status)) {
            return "Test executed successfully ✅";
        } else if ("FAIL".equals(status)) {
            String errorMessage = "Unknown error";
            
            if (result.getThrowable() != null) {
                errorMessage = result.getThrowable().getMessage();
                
                // Truncate long error messages
                if (errorMessage != null && errorMessage.length() > 100) {
                    // Remove "Expected condition" verbose part
                    if (errorMessage.contains("Expected condition")) {
                        int idx = errorMessage.indexOf("Expected condition");
                        errorMessage = errorMessage.substring(0, idx).trim();
                    }
                    if (errorMessage.length() > 100) {
                        errorMessage = errorMessage.substring(0, 100) + "...";
                    }
                }
            }
            
            return "❌ " + (errorMessage != null ? errorMessage : "Test failed");
        } else if ("SKIPPED".equals(status)) {
            return "Test skipped ⚠️";
        }
        return "-";
    }

    /**
     * Send HTML email report
     */
    private void sendHtmlEmail(String htmlContent) {
        logger.info("Preparing to send HTML email report...");
        
        String host = ConfigReader.getEmailSmtpHost();
        String from = ConfigReader.getEmailFrom();
        String password = ConfigReader.getEmailPassword();
        String[] toEmails = ConfigReader.getEmailRecipients();

        // Log configuration
        logger.info("Email Configuration:");
        logger.info("   SMTP Host: {}", host);
        logger.info("   From: {}", from != null ? from : "NOT SET");
        logger.info("   Recipients: {}", Arrays.toString(toEmails));

        // Validate credentials
        if (from == null || from.isEmpty() || from.startsWith("${")) {
            logger.error("❌ EMAIL_FROM not configured!");
            logger.error("   Set environment variable EMAIL_FROM or update config.properties");
            logger.info("📋 HTML Report content generated but not sent.");
            return;
        }

        if (password == null || password.isEmpty() || password.startsWith("${")) {
            logger.error("❌ EMAIL_PASSWORD not configured!");
            logger.error("   Set environment variable EMAIL_PASSWORD or update config.properties");
            logger.info("📋 HTML Report content generated but not sent.");
            return;
        }

        // Email properties - Using privateemail.com settings
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "mail.privateemail.com");

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

            // Subject
            message.setSubject("🧪 Automation Test Report - Shofha Subscription Flow");

            // HTML content as body
            message.setContent(htmlContent, "text/html; charset=utf-8");

            // Send
            logger.info("Sending email...");
            Transport.send(message);

            logger.info("========================================");
            logger.info("✅ HTML Report Email sent successfully!");
            logger.info("   Recipients: {}", Arrays.toString(toEmails));
            logger.info("========================================");

        } catch (AuthenticationFailedException authEx) {
            logger.error("❌ Email authentication failed!");
            logger.error("   Check EMAIL_FROM and EMAIL_PASSWORD");
            logger.error("   For Gmail, use App Password: https://myaccount.google.com/apppasswords");
        } catch (Exception e) {
            logger.error("❌ Failed to send email: {}", e.getMessage());
            logger.error("Stack trace:", e);
        }
    }
}