package Pages;

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
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // Define Elements
    @FindBy(id = "recipients")
    WebElement recipientsList;

    @FindBy(css = "#recipients li a")
    WebElement senderNumbers;

    @FindBy(css = "#messages .message-item")
    List<WebElement> messageItems;

    // Define Functions

    public String getOTP() {
        return OTP;
    }

    public void findSenderMessage() {
        try {
            System.out.println("Waiting for the sender message...");
            wait.until(ExpectedConditions.visibilityOf(senderNumbers)).click();
            System.out.println("OTP message opened successfully");
        } catch (Exception e) {
            System.out.println("Can't open the sender message. " + e.getMessage());
            Assert.fail("Sender message not found." + e.getMessage());
        }
    }

    public String extractOTP(String subscriptionTimestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime subscriptionTime = LocalDateTime.parse(subscriptionTimestamp, formatter);

        int retries = 5;
        int waitTimeInSeconds = 30;

        for (int attempt = 1; attempt <= retries; attempt++) {
            System.out.println("🔍 Attempt #" + attempt + " to find OTP...");

            try {
                // تحديث قائمة الرسائل (اختياري لو الصفحة مش بتتحدث تلقائيًا)
                wait.until(ExpectedConditions.visibilityOfAllElements(messageItems));

                for (WebElement message : messageItems) {
                    String messageTime = message.findElement(By.className("sms-time")).getText().trim();
                    LocalDateTime msgTime = LocalDateTime.parse(messageTime, formatter);

                    if (msgTime.isAfter(subscriptionTime)) {
                        WebElement body = message.findElement(By.className("sms-body"));
                        String text = body.getText();

                        // استخراج OTP من الرسالة
                        Pattern pattern = Pattern.compile("\\b\\d{4,6}\\b");
                        Matcher matcher = pattern.matcher(text);

                        if (matcher.find()) {
                            OTP = matcher.group();
                            System.out.println("✅ OTP found: " + OTP+ "\n Subscription time is: "+ subscriptionTime+ "\n Message time is: " +msgTime);
                            return OTP;
                        }
                    }
                }

                // لو مفيش OTP لحد دلوقتي
                if (attempt < retries) {
                    System.out.println("⏳ No OTP found yet. Waiting " + waitTimeInSeconds + " seconds before retrying...");
                    Thread.sleep(waitTimeInSeconds * 1000L);
                }

            } catch (Exception e) {
                System.out.println("⚠️ Error while checking messages: " + e.getMessage());
            }
        }

        Assert.fail("❌ OTP was not received after " + retries + " attempts.");
        return null;
    }

}