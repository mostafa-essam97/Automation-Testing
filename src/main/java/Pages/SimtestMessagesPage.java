package Pages;

import Utilities.TestContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SimtestMessagesPage - Page Object for SMS messages in SIMTest
 */
public class SimtestMessagesPage extends BasePage {

    private static final Pattern OTP_PATTERN = Pattern.compile("\\b\\d{4,6}\\b");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int MAX_RETRIES = 5;
    private static final int RETRY_WAIT_SECONDS = 30;

    public SimtestMessagesPage(WebDriver driver) {
        super(driver);
    }

    // ============ Elements ============

    @FindBy(id = "recipients")
    private WebElement recipientsList;

    @FindBy(css = "#recipients li a")
    private WebElement senderNumbers;

    @FindBy(css = "#messages .message-item")
    private List<WebElement> messageItems;

    // ============ Actions ============

    /**
     * Find and click on sender message
     */
    public void findSenderMessage() {
        try {
            logger.info("Waiting for sender message...");
            click(senderNumbers);
            logger.info("✅ OTP message opened");
        } catch (Exception e) {
            logger.error("❌ Cannot open sender message: {}", e.getMessage());
            Assert.fail("Sender message not found: " + e.getMessage());
        }
    }

    /**
     * Extract OTP from all senders after subscription time
     */
    public void extractOTPFromAllSenders() {
        // Get subscription timestamp
        String subscriptionTimestamp = TestContext.getData().getSubscriptionTimestamp();
        if (subscriptionTimestamp == null || subscriptionTimestamp.isEmpty()) {
            Assert.fail("❌ Subscription timestamp is missing. Cannot extract OTP.");
        }

        LocalDateTime subscriptionTime = LocalDateTime.parse(subscriptionTimestamp, DATE_FORMATTER);
        int subMinutes = subscriptionTime.getMinute();
        int subSeconds = subscriptionTime.getSecond();

        logger.info("🔍 Looking for OTP after subscription time: {}", subscriptionTime);

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            logger.info("Attempt #{} to find OTP", attempt);

            try {
                // Get all senders
                List<WebElement> senders = driver.findElements(By.cssSelector("#recipients li"));

                for (WebElement sender : senders) {
                    sender.click();
                    waitForPageStability();

                    // Get messages for this sender
                    List<WebElement> messages = driver.findElements(By.cssSelector("#messages .message-item"));

                    if (messages.isEmpty()) {
                        logger.debug("⚠️ No messages for sender: {}", sender.getText());
                        continue;
                    }

                    // Check each message
                    for (WebElement message : messages) {
                        String otp = extractOTPFromMessage(message, subMinutes, subSeconds);
                        if (otp != null) {
                            TestContext.getData().setOtpCode(otp);
                            logger.info("✅ OTP found: {} from sender: {}", otp, sender.getText());
                            return;
                        }
                    }
                }

                // Wait before retry
                if (attempt < MAX_RETRIES) {
                    logger.info("⏳ No OTP found. Waiting {} seconds...", RETRY_WAIT_SECONDS);
                    waitSeconds(RETRY_WAIT_SECONDS);
                }

            } catch (Exception e) {
                logger.warn("⚠️ Error checking messages: {}", e.getMessage());
            }
        }

        Assert.fail("❌ OTP not received after " + MAX_RETRIES + " attempts");
    }

    /**
     * Extract OTP from a single message if it's after subscription time
     */
    private String extractOTPFromMessage(WebElement message, int subMinutes, int subSeconds) {
        try {
            String messageTime = message.findElement(By.className("sms-time")).getText().trim();
            LocalDateTime msgTime = LocalDateTime.parse(messageTime, DATE_FORMATTER);

            int msgMinutes = msgTime.getMinute();
            int msgSeconds = msgTime.getSecond();

            // Check if message is after subscription
            boolean isAfterSubscription = (msgMinutes > subMinutes) ||
                    (msgMinutes == subMinutes && msgSeconds >= subSeconds);

            if (isAfterSubscription) {
                WebElement body = message.findElement(By.className("sms-body"));
                String text = body.getText();

                Matcher matcher = OTP_PATTERN.matcher(text);
                if (matcher.find()) {
                    return matcher.group();
                } else {
                    logger.debug("⚠️ Message at {} has no OTP", msgTime);
                }
            } else {
                logger.debug("⏳ Message at {} is before subscription time", msgTime);
            }
        } catch (Exception e) {
            logger.debug("Error parsing message: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Get the extracted OTP code
     */
    public String getOTPCode() {
        return TestContext.getData().getOtpCode();
    }

    /**
     * Wait for specified seconds
     */
    private void waitSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}






