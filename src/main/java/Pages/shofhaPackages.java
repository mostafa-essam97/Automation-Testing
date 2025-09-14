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

public class shofhaPackages extends pageBase {
    public String subscriptionTimestamp = "";
//    public String packageType = "";
//    public String packagePrice = "";

    public shofhaPackages(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    //Define Elements
    @FindBy(css = "div.PKG .col-md-4")
    List<WebElement> packages;

    @FindBy(id = "errortext1")
    WebElement packagesErrorMsg;

    //Define Functions
    public void selectPackage(int index) {
        try {
            // تأكد إن الباقات كلها ظهرت
            wait.until(ExpectedConditions.visibilityOfAllElements(packages));

            if (packages.isEmpty()) {
                System.out.println("❌ No packages appeared on the page!");
                Assert.fail("❌ No packages available to select.");
                return;
            }

            if (index < 0 || index >= packages.size()) {
                System.out.println("❌ Invalid index. Available packages: " + packages.size());
                Assert.fail("❌ Invalid index: '" + index + "'. Available packages: '" + packages.size() + "'.");
            } else {
                System.out.println("👉 You are going to subscribe with the " + getOrdinal(index + 1) + " package.");
                WebElement selectedPackage = packages.get(index);

                // جبت نوع الباقة (Daily, Weekly, Monthly ...)
                String packageType = selectedPackage.findElement(By.tagName("span")).getText().trim();
                // جبت سعر الباقة
                String packagePrice = selectedPackage.findElement(By.className("CustomPrice")).getText().trim();

                // سجلت وقت الاشتراك
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                subscriptionTimestamp = LocalDateTime.now().format(formatter);
                testDataHolder.subscriptionTimeStampData = subscriptionTimestamp;
                testDataHolder.packageTypeData = packageType;
                testDataHolder.packagePriceData = packagePrice;

                // زرار Subscribe
                WebElement subNowBtn = selectedPackage.findElement(By.tagName("button"));
                wait.until(ExpectedConditions.elementToBeClickable(subNowBtn));
                subNowBtn.click();
                Thread.sleep(1000);

                // ✅ هنا استخدمنا الفاكشن من pageBase
                if (isElementDisplayed(packagesErrorMsg)) {
                    System.out.println("❌ Error message appeared after selecting package: " + packagesErrorMsg.getText());
                    Assert.fail("❌ Subscription failed. Error message: " + packagesErrorMsg.getText());
                    return;
                }

                System.out.println("✅ You have chosen the " + packageType + " with price " + packagePrice + ".\n" +
                        "Please insert a valid OTP to enjoy with SHOFHA.\n" +
                        "⏱️ Subscription time recorded: " + subscriptionTimestamp);
            }
        } catch (Exception e) {
            System.out.println("❌ Failed to select package. " + e.getMessage());
            Assert.fail("❌ Exception while selecting package: " + e.getMessage());
        }
    }
}