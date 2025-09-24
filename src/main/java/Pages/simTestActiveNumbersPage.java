package Pages;

import Utilities.testDataHolder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class simTestActiveNumbersPage extends pageBase{
    public simTestActiveNumbersPage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    //Define Elements

    //Define Functions
    public void verifyReservedNumberInActiveList(String expectedNumber) {
        System.out.println("🔍 Waiting for number to appear in ACTIVE list: '" + expectedNumber + "'");

        int maxRetries = 10; // نحاول 10 مرات كل 30 ثانية
        int retryCount = 0;
        boolean found = false;

        while (retryCount < maxRetries && !found) {
            driver.navigate().refresh();
            System.out.println("🔄 Refreshed the page | Attempt #" + (retryCount + 1));

            try {
                // ✅ استنى العناصر تتحدث بعد الريفريش
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("active_reservations")));

                // ننتظر ظهور قائمة ACTIVE
                List<WebElement> activeList = wait.until(
                        ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("#active_reservations li")));
                if (activeList.isEmpty()){
                    System.out.println("⏳ Empty active list, retrying in 30 seconds...");
                    Thread.sleep(20000);
                    System.out.println("🔄 Refreshed the page | Attempt #" + (retryCount + 1));
                    retryCount++;
                    continue;
                }

                for (WebElement item : activeList) {
                    String itemText = item.getText().trim();
                    System.out.println("🧪 Comparing item text: [" + itemText + "] with expected: [" + expectedNumber + "]");

                    if (expectedNumber.replaceAll("\\s+", "").contains(itemText.replaceAll("\\s+", ""))) {
                        System.out.println("✅ Found number in ACTIVE list: " + itemText);
                        System.out.println("The reserved number is ==> '" + testDataHolder.fullReservedNumberData + "'");
                        WebElement link = item.findElement(By.tagName("a"));
                        link.click(); // نضغط على الرقم
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    retryCount++;
                    System.out.println("⏳ The reserved number not found yet, retrying in 30 seconds...");

                    Thread.sleep(30000);
                }

            } catch (Exception e) {
                retryCount++;
                System.out.println("⚠️ Error during check Empty active list: " + e.getMessage() + " | Retrying...");
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException ignored) {
                }
            }
        }
        if (!found) {
            Assert.fail("❌ Reserved number '" + expectedNumber + "' not found in ACTIVE list after " + maxRetries + " attempts.");
        }
    }
}
