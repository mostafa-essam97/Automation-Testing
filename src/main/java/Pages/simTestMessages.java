package Pages;

import Utilities.testDataHolder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class simTestMessages extends pageBase {
    public String OTP = "";

    public simTestMessages(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(50));
    }

    // Define Elements
    @FindBy(id = "recipients")
    WebElement recipientsList;

    @FindBy(css = "#recipients li a")
    WebElement senderNumbers;

    @FindBy(css = "#messages .message-item")
    List<WebElement> messageItems;

    // Define Functions
    public void findSenderMessage() {
        try {
            System.out.println("Waiting for the sender message...");
            wait.until(ExpectedConditions.visibilityOf(senderNumbers)).click();
            System.out.println("OTP message opened successfully.");
        } catch (Exception e) {
            System.out.println("Can't open the sender message. " + e.getMessage());
            Assert.fail("Sender message not found." + e.getMessage());
        }
    }

    public void extractOTPFromAllSenders() {
        // ✅ 1. وقت الاشتراك
        String subscriptionTimestamp = testDataHolder.subscriptionTimeStampData;
        if (subscriptionTimestamp == null || subscriptionTimestamp.isEmpty()) {
            Assert.fail("❌ Subscription timestamp is missing. Cannot proceed with OTP extraction.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime subscriptionTime = LocalDateTime.parse(subscriptionTimestamp, formatter);

        int subMinutes = subscriptionTime.getMinute();
        int subSeconds = subscriptionTime.getSecond();

        int retries = 5;
        int waitTimeInSeconds = 30;

        for (int attempt = 1; attempt <= retries; attempt++) {
            System.out.println("🔍 Attempt #" + attempt + " to find OTP after subscription time: " + subscriptionTime);

            try {
                // ✅ 2. نجيب كل المرسلين
                List<WebElement> senders = driver.findElements(By.cssSelector("#recipients li"));
                boolean otpFound = false;

                for (WebElement sender : senders) {
                    sender.click();
                    Thread.sleep(2000); // نستنى شوية لحد ما الرسائل تتحمل

                    // ✅ 3. نجيب كل الرسائل الخاصة بالمرسل
                    List<WebElement> messages = driver.findElements(By.cssSelector("#messages .message-item"));

                    if (messages.isEmpty()) {
                        System.out.println("⚠️ No messages for sender: " + sender.getText());
                        continue;
                    }

                    // ✅ 4. نمشي على كل رسالة
                    for (WebElement message : messages) {
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

                            // regex لاستخراج OTP
                            Pattern pattern = Pattern.compile("\\b\\d{4,6}\\b");
                            Matcher matcher = pattern.matcher(text);

                            if (matcher.find()) {
                                OTP = matcher.group();
                                testDataHolder.otpCodeData = OTP;
                                System.out.println("✅ OTP found: " + OTP + " from sender: " + sender.getText() + " at " + msgTime);
                                otpFound = true;
                                break; // وقف أول ما نلاقي OTP
                            } else {
                                System.out.println("⚠️ Message at " + msgTime + " has no OTP.");
                            }
                        } else {
                            System.out.println("⏳ Message at " + msgTime + " is before subscription time (minute-second compare). Ignoring...");
                        }
                    }

                    if (otpFound) {
                        return; // وقف أول ما نلاقي الكود
                    }
                }

                // ✅ 5. انتظار قبل إعادة المحاولة
                if (attempt < retries) {
                    System.out.println("⏳ No OTP found. Waiting " + waitTimeInSeconds + " seconds before retrying...");
                    Thread.sleep(waitTimeInSeconds * 1000L);
                }

            } catch (Exception e) {
                System.out.println("⚠️ Error while checking senders/messages: " + e.getMessage());
            }
        }

        Assert.fail("❌ OTP was not received after " + retries + " attempts.");
    }
}