package Pages;

import Utilities.TestContext;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
 * SimTestMessages - Page Object for SIMTest messages/SMS section
 */
public class simTestMessages extends BasePage {
    private String OTP = "";

    public simTestMessages(WebDriver driver) {
        super(driver);
    }

    // ============ Elements ============

    @FindBy(id = "recipients")
    private WebElement recipientsList;

    @FindBy(css = "#recipients li a")
    private WebElement senderNumbers;

    @FindBy(css = "#messages .message-item")
    private List<WebElement> messageItems;

    @FindBy(id = "active_reservations")
    private WebElement activeReservationsList;

    // ============ Actions ============

    /**
     * Select the correct country from Active Reservations list based on MSISDN verification
     * This method clicks on each reservation, reads the displayed MSISDN, and compares with our reserved number
     */
    public void selectCorrectCountryFromActiveList() {
        String reservedNumber = TestContext.getData().getFullReservedNumber();
        
        logger.info("========================================");
        logger.info("Selecting correct country from Active list");
        logger.info("   Looking for MSISDN: {}", reservedNumber);
        logger.info("========================================");

        if (reservedNumber == null || reservedNumber.isEmpty()) {
            logger.error("❌ Reserved number is NULL or empty!");
            Assert.fail("Reserved number not found in TestContext");
        }

        try {
            // Wait for active reservations list
            longWait.until(ExpectedConditions.presenceOfElementLocated(By.id("active_reservations")));
            
            // Get all active reservations
            List<WebElement> activeReservations = driver.findElements(
                By.cssSelector("#active_reservations li")
            );
            
            logger.info("Found {} active reservations", activeReservations.size());

            if (activeReservations.isEmpty()) {
                logger.error("❌ No active reservations found!");
                Assert.fail("No active reservations in the list");
            }

            // If only one reservation, just click it and verify
            if (activeReservations.size() == 1) {
                logger.info("Only one reservation found, clicking it...");
                WebElement link = activeReservations.get(0).findElement(By.tagName("a"));
                clickWithJS(link);
                waitSeconds(2);
                
                // Verify MSISDN
                String displayedMsisdn = getDisplayedMsisdn();
                if (isMsisdnMatch(displayedMsisdn, reservedNumber)) {
                    logger.info("✅ MSISDN verified: {}", displayedMsisdn);
                } else {
                    logger.warn("⚠️ MSISDN mismatch! Expected: {}, Got: {}", reservedNumber, displayedMsisdn);
                }
                return;
            }

            // Multiple reservations - check each one by MSISDN
            for (int i = 0; i < activeReservations.size(); i++) {
                WebElement reservation = activeReservations.get(i);
                String reservationName = reservation.getText().trim();
                
                logger.info("Checking reservation #{}: '{}'", i + 1, reservationName);
                
                // Click on the reservation
                WebElement link = reservation.findElement(By.tagName("a"));
                clickWithJS(link);
                waitSeconds(2);
                
                // Read the displayed MSISDN
                String displayedMsisdn = getDisplayedMsisdn();
                logger.info("   Displayed MSISDN: {}", displayedMsisdn);
                
                // Compare with our reserved number
                if (isMsisdnMatch(displayedMsisdn, reservedNumber)) {
                    logger.info("✅ Found matching number!");
                    logger.info("   Reservation: {}", reservationName);
                    logger.info("   MSISDN: {}", displayedMsisdn);
                    return;
                }
                
                logger.info("   ❌ Not matching, trying next...");
            }

            // If we get here, no match was found
            logger.error("❌ Could not find reserved number {} in any active reservation!", reservedNumber);
            logger.error("   Checked {} reservations", activeReservations.size());
            Assert.fail("Reserved number not found in active reservations list");

        } catch (Exception e) {
            logger.error("❌ Error selecting country: {}", e.getMessage());
            Assert.fail("Failed to select country from active list: " + e.getMessage());
        }
    }

    /**
     * Get the displayed MSISDN from the page
     */
    private String getDisplayedMsisdn() {
        try {
            WebElement msisdnElement = driver.findElement(
                By.cssSelector("div[data-bind='html: msisdn']")
            );
            return msisdnElement.getText().trim();
        } catch (Exception e) {
            logger.warn("Could not find MSISDN element: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Check if two MSISDNs match (handles different formats)
     */
    private boolean isMsisdnMatch(String displayed, String reserved) {
        if (displayed == null || reserved == null) {
            return false;
        }
        
        // Remove all non-digit characters for comparison
        String displayedDigits = displayed.replaceAll("[^0-9]", "");
        String reservedDigits = reserved.replaceAll("[^0-9]", "");
        
        // Check for exact match or if one contains the other
        return displayedDigits.equals(reservedDigits) ||
               displayedDigits.endsWith(reservedDigits) ||
               reservedDigits.endsWith(displayedDigits) ||
               displayedDigits.contains(reservedDigits) ||
               reservedDigits.contains(displayedDigits);
    }

    /**
     * Click using JavaScript (more reliable)
     */
    private void clickWithJS(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    /**
     * Find and click on sender message
     */
    public void findSenderMessage() {
        // First, select the correct country from active list
        selectCorrectCountryFromActiveList();
        
        logger.info("Waiting for sender message to appear...");
        try {
            longWait.until(ExpectedConditions.visibilityOf(senderNumbers)).click();
            logger.info("✅ OTP message opened successfully");
        } catch (Exception e) {
            logger.error("❌ Can't open sender message: {}", e.getMessage());
            logger.error("   Possible causes:");
            logger.error("   1. No SMS received yet - OTP may not have been sent");
            logger.error("   2. Wrong phone number was used for subscription");
            logger.error("   3. Network delay in receiving SMS");
            Assert.fail("Sender message not found. " + e.getMessage());
        }
    }

    /**
     * Extract OTP from all senders' messages
     */
    public void extractOTPFromAllSenders() {
        logger.info("Starting OTP extraction from messages...");

        // 1. Get subscription timestamp from TestContext
        String subscriptionTimestamp = TestContext.getData().getSubscriptionTimestamp();
        
        if (subscriptionTimestamp == null || subscriptionTimestamp.isEmpty()) {
            logger.error("❌ Subscription timestamp is missing!");
            logger.error("   This means TC_03 (Subscribe) did not store the timestamp correctly.");
            Assert.fail("Subscription timestamp is missing. Cannot proceed with OTP extraction.");
        }

        logger.info("Subscription timestamp: {}", subscriptionTimestamp);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime subscriptionTime = LocalDateTime.parse(subscriptionTimestamp, formatter);

        int subMinutes = subscriptionTime.getMinute();
        int subSeconds = subscriptionTime.getSecond();

        int retries = 5;
        int waitTimeInSeconds = 30;

        for (int attempt = 1; attempt <= retries; attempt++) {
            logger.info("🔍 Attempt #{} to find OTP after subscription time: {}", attempt, subscriptionTime);

            try {
                // Refresh page to get new messages
                if (attempt > 1) {
                    driver.navigate().refresh();
                    waitSeconds(3);
                }

                // 2. Get all senders
                List<WebElement> senders = driver.findElements(By.cssSelector("#recipients li"));
                logger.info("Found {} senders", senders.size());
                
                boolean otpFound = false;

                for (WebElement sender : senders) {
                    String senderName = sender.getText().trim();
                    logger.debug("Checking sender: {}", senderName);
                    
                    sender.click();
                    waitSeconds(2);

                    // 3. Get all messages for this sender
                    List<WebElement> messages = driver.findElements(By.cssSelector("#messages .message-item"));

                    if (messages.isEmpty()) {
                        logger.warn("⚠️ No messages for sender: {}", senderName);
                        continue;
                    }

                    logger.debug("Found {} messages for sender: {}", messages.size(), senderName);

                    // 4. Check each message
                    for (WebElement message : messages) {
                        try {
                            String messageTime = message.findElement(By.className("sms-time")).getText().trim();
                            LocalDateTime msgTime = LocalDateTime.parse(messageTime, formatter);

                            int msgMinutes = msgTime.getMinute();
                            int msgSeconds = msgTime.getSecond();

                            boolean isAfterSubscription =
                                    (msgMinutes > subMinutes) ||
                                            (msgMinutes == subMinutes && msgSeconds >= subSeconds);

                            if (isAfterSubscription) {
                                WebElement body = message.findElement(By.className("sms-body"));
                                String text = body.getText();
                                logger.debug("Message body: {}", text);

                                // Extract OTP using regex
                                Pattern pattern = Pattern.compile("\\b\\d{4,6}\\b");
                                Matcher matcher = pattern.matcher(text);

                                if (matcher.find()) {
                                    OTP = matcher.group();
                                    
                                    // Store OTP in TestContext
                                    TestContext.getData().setOtpCode(OTP);
                                    
                                    logger.info("✅ OTP found: {} from sender: {} at {}", OTP, senderName, msgTime);
                                    otpFound = true;
                                    break;
                                } else {
                                    logger.debug("⚠️ Message at {} has no OTP pattern", msgTime);
                                }
                            } else {
                                logger.debug("⏳ Message at {} is before subscription time. Ignoring...", msgTime);
                            }
                        } catch (Exception msgEx) {
                            logger.warn("Error parsing message: {}", msgEx.getMessage());
                        }
                    }

                    if (otpFound) {
                        logger.info("✅ OTP extraction successful!");
                        return;
                    }
                }

                // 5. Wait before retry
                if (attempt < retries) {
                    logger.info("⏳ No OTP found. Waiting {} seconds before retry...", waitTimeInSeconds);
                    waitSeconds(waitTimeInSeconds);
                }

            } catch (Exception e) {
                logger.error("⚠️ Error while checking senders/messages: {}", e.getMessage());
            }
        }

        logger.error("❌ OTP was not received after {} attempts", retries);
        logger.error("   Possible causes:");
        logger.error("   1. SMS was not sent by the service");
        logger.error("   2. Wrong phone number used");
        logger.error("   3. Network issues preventing SMS delivery");
        logger.error("   4. OTP message format changed and doesn't match pattern");
        Assert.fail("OTP was not received after " + retries + " attempts.");
    }

    /**
     * Get the extracted OTP
     */
    public String getOTP() {
        return OTP;
    }

    /**
     * Wait for specified seconds
     */
    private void waitSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}